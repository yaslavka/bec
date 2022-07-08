package matrixbackend.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import matrixbackend.MatrixEssenceEnum;
import matrixbackend.SideMatrix;
import matrixbackend.dto.*;
import matrixbackend.entity.*;

import matrixbackend.entity.QMatrix;
import matrixbackend.helper.BinaryTree;
import matrixbackend.helper.Node;
import matrixbackend.jwt.JwtTokenProvider;
import matrixbackend.repository.*;
import matrixbackend.utils.Finans;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatrixService {

    @Autowired
    MatrixTypeRepository matrixTypeRepository;

    @Autowired
    MatrixRepository matrixRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;


    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    BinaryTree binaryTree;

    @Autowired
    CloneStatRepository cloneStatRepository;


    @Autowired
    MatrixTableRepository matrixTableRepository;

    @Autowired
    MarketingService marketingService;

    @Autowired
    Finans finans;


    public List<MatrixTypeDTO> getAllMatrixType(String token){

        String login = jwtTokenProvider.getLoginFromToken(token.substring(7));
        Optional <User> user=userRepository.findByUsername(login);



        List<TypeMatrix> typeMatrixList =matrixTypeRepository.findAll();
        List<MatrixTypeDTO> matrixTypeDTOList= typeMatrixList.stream()
                .map(typeMatrix -> new MatrixTypeDTO(typeMatrix,
                        (matrixTableRepository.findByUserAndTypeMatrixId(user.get(), typeMatrix.getId()).isPresent())? matrixTableRepository.findByUserAndTypeMatrixId(user.get(), typeMatrix.getId()).get().getCount() : 0 ))
                .collect(Collectors.toList());
        return matrixTypeDTOList;


    }


    @Transactional
    public boolean createNewMatrix(String token, Long matrix_type) throws Exception {

        boolean result = false;

        String login = jwtTokenProvider.getLoginFromToken(token.substring(7));
        Optional <User> user=userRepository.findByUsername(login);


        int cost=matrixTypeRepository.getById(matrix_type).getSum();

        int currentBalance=user.get().getBalance();

        //проверка баланса перед созданием записей в бд
        if(currentBalance>=cost) {

            TypeMatrix typeMatrix = matrixTypeRepository.getById(matrix_type);
            List<Matrix> matrices = new ArrayList<>();
            Optional<MatrixTable> matrixTableExist = matrixTableRepository.findByUserAndTypeMatrixId(user.get(), matrix_type);


            Matrix newMatrix = Matrix.builder()
                    .user(user.get())
                    .build();


            // Check if first matrixTable exist

            //Marketing clones adding
            matrixTableExist.ifPresentOrElse((matrixTable) -> {
                        int clones = matrixTable.getCount();
                        clones += 1;
                        matrixTable.setCount(clones);
                        matrixTableRepository.save(matrixTable);
                    },
                    () -> {
                        Optional<MatrixTable> getParentMatrixTable = matrixTableRepository.findByUserAndTypeMatrixId(user.get().getReferral(), matrix_type);
                        getParentMatrixTable.ifPresentOrElse((matrixParentTable) -> {
                                    matrices.addAll(matrixRepository.findByParentMatrixId(matrixParentTable.getMatrixParent().getId()));
                                    Node parent = new Node(matrixParentTable.getMatrixParent());
                                    matrices.add(0, matrixParentTable.getMatrixParent());
                                    matrices.add(matrices.size(), newMatrix);
                                    binaryTree.root = binaryTree.insertLevelOrderMatrix(matrices, parent, 0);
                                    binaryTree.traverseLevelOrder();
                                    matrixRepository.save(newMatrix);

                                    //сделать проверку на размер матрицы, возможно у матрицы к торой присоединяем уже нужный рамер и надо дать поощерение
                            
                                },
                                () -> {
                                    new Exception();
                                }
                        );
                        MatrixTable matrixTableNew = MatrixTable.builder()
                                .count(0)
                                .typeMatrix(typeMatrix)
                                .IsActive(true)
                                .matrixParent(newMatrix)
                                .user(user.get())
                                .build();
                        matrixTableRepository.save(matrixTableNew);
                        matrixRepository.save(newMatrix);
                        return;
                    });

            // Marketing


            marketingService.writeOffFromBalance(user.get(), matrix_type);

            result = true;
        }

        return result;
    };


    public JSONObject   getAllActivatedMatrices(
            Long matrix_id,
            String token,
            Long matrix_type) throws Exception {

        binaryTree.root = new Node(); // Сброс дерева

        String login = jwtTokenProvider.getLoginFromToken(token.substring(7));
        User user = userRepository.
                findByUsername(login).
                orElseThrow(Exception::new);



        LinkedList<Matrix> matrices = new LinkedList<>();
        LinkedList<Matrix> linkedList = new LinkedList<>();
        JSONObject jsonObject = new JSONObject();

        if (matrix_id != null) {
            // Find by matrix_id
            LinkedList<Matrix> matricesByParentMatrixId = matrixRepository.findByParentMatrixId(matrix_id);
            matrices.addAll(matricesByParentMatrixId);
            Optional<Matrix> matrixExist = matrixRepository.findById(matrix_id);

            matrixExist.ifPresentOrElse((matrixFound) -> {

                        binaryTree.root = new Node(matrixFound);
                    },
                    () -> {
                        // throw exception if matrix not found
                    });

            // Binary Tree
            matrices.addFirst(binaryTree.root.parent);


            for (int i = 1; i < matrices.size(); i++) {
                binaryTree.addRecursive(binaryTree.root, matrices.get(i));
            }


//


            linkedList.addAll(binaryTree.traverseLevelOrder());


        } else if(matrix_type!=null) {
            // Find parent  matrix
            matrixTableRepository.findByUserAndTypeMatrixId(user,matrix_type)
                    .ifPresentOrElse(
                            (matrixTable) -> {
                                Optional.ofNullable(matrixTable.getMatrixParent())
                                        .ifPresentOrElse((matrix->{
                                            LinkedList<Matrix> matricesByParentMatrixId = matrixRepository
                                                    .findByParentMatrixId(matrix.getId());
                                            matrices.addAll(matricesByParentMatrixId);
                                            binaryTree.root = new Node(matrix);
                                            matrices.addFirst(binaryTree.root.parent);


                                            for (int i = 1; i < matrices.size(); i++) {
                                                    binaryTree.addRecursive(binaryTree.root, matrices.get(i));
                                                }

//                                                    System.out.println(binaryTree.root.left.right.parent.getId());


                                                    linkedList.addAll(binaryTree.traverseLevelOrderToList());


                                                }),
                                                ()->{
                                        });
                                },
                            ()->{
                            });


        binaryTree.traverseLevelOrderToList();




    }

        Optional.ofNullable(binaryTree.root.parent).ifPresentOrElse(first -> {
                    jsonObject.put(Integer.toString(0),new MatrixDTO(binaryTree.root.parent));

                    Optional.ofNullable(binaryTree.root.left).ifPresentOrElse(second -> {
                                jsonObject.put(Integer.toString(1),new MatrixDTO(binaryTree.root.left.parent));


                                Optional.ofNullable(binaryTree.root.left.left).ifPresentOrElse(fourth -> {
                                            jsonObject.put(Integer.toString(3),new MatrixDTO(binaryTree.root.left.left.parent));
                                        },
                                        ()->{
                                            jsonObject.put(Integer.toString(3),null);
                                        });

                                Optional.ofNullable(binaryTree.root.left.right).ifPresentOrElse(fourth -> {
                                            jsonObject.put(Integer.toString(4),new MatrixDTO(binaryTree.root.left.right.parent));
                                        },
                                        ()->{
                                            jsonObject.put(Integer.toString(4),null);
                                        });
                            },
                            ()->{
                                jsonObject.put(Integer.toString(1),null);
                                jsonObject.put(Integer.toString(3),null);
                                jsonObject.put(Integer.toString(4),null);
                            });
                },
                ()->{
                    jsonObject.put(Integer.toString(0),null);
                });


        Optional.ofNullable(binaryTree.root.right).ifPresentOrElse(second -> {
                    jsonObject.put(Integer.toString(2),new MatrixDTO(binaryTree.root.right.parent));

                    Optional.ofNullable(binaryTree.root.right.left).ifPresentOrElse(sixth -> {
                                jsonObject.put(Integer.toString(5),new MatrixDTO(binaryTree.root.right.left.parent));
                            },
                            ()->{
                                jsonObject.put(Integer.toString(5),null);
                            });


                    Optional.ofNullable(binaryTree.root.right.right).ifPresentOrElse(seven -> {
                                jsonObject.put(Integer.toString(6),new MatrixDTO(binaryTree.root.right.right.parent));
                            },
                            ()->{
                                jsonObject.put(Integer.toString(6),null);
                            });
                },
                ()->{
                    jsonObject.put(Integer.toString(2),null);
                    jsonObject.put(Integer.toString(5),null);
                    jsonObject.put(Integer.toString(6),null);
                });
        jsonObject.put("count", 7);
        return jsonObject;
        }

    public List<CloneDTO> getAllClones(String token, Predicate predicate, Long matrix_type) {
        String login=jwtTokenProvider.getLoginFromToken(token.substring(7));
        QMatrix qMatrix=QMatrix.matrix;
        final BooleanBuilder builder = new BooleanBuilder(predicate);
        builder.and(qMatrix.user.username.eq(login).and(qMatrix.matrixEssenceEnum.eq(MatrixEssenceEnum.Clone)));
        List <Matrix> matrices=matrixRepository.findAll(builder.getValue());
        List<CloneDTO> cloneDTOList=matrices.stream().
                map(clone-> new CloneDTO(clone))
                .collect(Collectors.toList());
        return cloneDTOList;
    }

    @Transactional
    public boolean installMatrixToBinaryTree(String token,
                                          InstallCloneRequestDTO installCloneRequestDTO) {
        boolean result = false;
        boolean doSafe = false;

        binaryTree.root=null;
        String login = jwtTokenProvider.getLoginFromToken(token.substring(7));
        Optional<User> user = userRepository.findByUsername(login);
        Optional <Matrix> matrixParent=matrixRepository.findById(installCloneRequestDTO.getAncestor_id());
        List <Matrix> matrixList=matrixRepository.findByParentMatrixId(matrixParent.get().getId());
        binaryTree.root=new Node(matrixParent.get());

        Matrix tempMatrix = matrixParent.get();

        //поиск самого головного элемента матрицы
        while (tempMatrix.getParentMatrix() != null) {
            tempMatrix = matrixRepository.findById(tempMatrix.getParentMatrix().getId()).get();
        }

        //получаем элемент из matrix_table для извлечения типа матрицы
        MatrixTable tempMatrixTable = matrixTableRepository.findByMatrixParent(tempMatrix).get();

        //получаем запись из matrix_table для конкретного пользователя
        MatrixTable matrixTable = matrixTableRepository.findByUserAndTypeMatrixId(user.get(), tempMatrixTable.getTypeMatrix().getId()).get();

        for (int i = 0; i <matrixList.size() ; i++) {
            binaryTree.addRecursive(binaryTree.root,matrixList.get(i));
        }



        int value=installCloneRequestDTO.getPlace();


        Matrix newMatrix= Matrix.builder()
                .user(user.get())
                .build();

         Matrix parentMatrix=null;

        switch (value){
            case 1:
                //binaryTree.root.left.parent=newMatrix;
                binaryTree.root.left = new Node(newMatrix);
                parentMatrix=binaryTree.root.parent;
                newMatrix.setParentMatrix(parentMatrix);
                newMatrix.setSideMatrix(SideMatrix.LEFT);
                doSafe = true;
                break;
            case 2:
                binaryTree.root.right=new Node(newMatrix);
                parentMatrix=binaryTree.root.parent;
                newMatrix.setParentMatrix(parentMatrix);
                newMatrix.setSideMatrix(SideMatrix.RIGHT);
                doSafe = true;
                break;
            case 3:
                if (binaryTree.root.right != null) {
                    binaryTree.root.left.left = new Node(newMatrix);

                    parentMatrix = binaryTree.root.left.parent;
                    newMatrix.setParentMatrix(parentMatrix);
                    newMatrix.setSideMatrix(SideMatrix.LEFT);

                    finans.IncreaseUserBalance(token, 500);
                    doSafe = true;
                }
                break;

            case 4:
                if (binaryTree.root.right != null) {
                    binaryTree.root.left.right = new Node(newMatrix);

                    parentMatrix = binaryTree.root.left.parent;
                    newMatrix.setParentMatrix(parentMatrix);
                    newMatrix.setSideMatrix(SideMatrix.RIGHT);
                    doSafe = true;
                }
                break;

            case 5:
                if (binaryTree.root.left != null) {
                    binaryTree.root.right.left = new Node(newMatrix);

                    parentMatrix = binaryTree.root.right.parent;
                    newMatrix.setParentMatrix(parentMatrix);
                    newMatrix.setSideMatrix(SideMatrix.LEFT);
                    doSafe = true;
                }
                break;

            case 6:
                if (binaryTree.root.left != null) {
                    binaryTree.root.right.right = new Node(newMatrix);

                    parentMatrix = binaryTree.root.right.parent;
                    newMatrix.setParentMatrix(parentMatrix);
                    newMatrix.setSideMatrix(SideMatrix.RIGHT);
                    doSafe = true;
                }
                break;
        }
        if (doSafe && matrixTable.getCount() >= 1) {
            matrixRepository.save(newMatrix);

            //вычитаю count при установке клона
            matrixTable.setCount(matrixTable.getCount()-1);
            matrixTableRepository.save(matrixTable);

            // если матрица заполнена, то выполняю проверку и выделение матрицы выше уровнем
            
            if (matrixRepository.findByParentMatrixId(matrixParent.get().getId()).size() == 6)
                checkUpMatrix(user.get(), matrixTable.getTypeMatrix().getId());

            result = true;
        }

        return result;
    }

    // проверка существования матрица высшего порядка, куплена ли она и присвоения, если не куплена
    public boolean checkUpMatrix (User user, Long matrix_type) {

        // берем текущий типа матрицы и проверяем есть тип матрицы со старшим идентификатором
        List<TypeMatrix> typeMatrix = matrixTypeRepository.findAllAndSortById();
        int i = 0;
        boolean isModified = false;
        for (TypeMatrix tM : typeMatrix) {
            i++;
            if (tM.getId() == matrix_type && i < typeMatrix.size()) {
                matrix_type = typeMatrix.get(i).getId();
                isModified = true;
                break;
            }
        }

        if (isModified) {
            TypeMatrix typeMatrixNew = matrixTypeRepository.getById(matrix_type);

            Optional<MatrixTable> matrixTableExist = matrixTableRepository.findByUserAndTypeMatrixId(user, typeMatrixNew.getId());

            matrixTableExist.ifPresentOrElse((matrixTable) -> {


                int clones = matrixTable.getCount();
                System.out.println("До добавления: " + clones);
                clones += 1;
                matrixTable.setCount(clones);
                matrixTableRepository.save(matrixTable);
                System.out.println("После добавления: " + clones);

            }, () -> {
                Matrix newMatrix = Matrix.builder()
                        .user(user)
                        .build();


                MatrixTable matrixTableNew = MatrixTable.builder()
                        .count(0)
                        .typeMatrix(typeMatrixNew)
                        .IsActive(true)
                        .matrixParent(newMatrix)
                        .user(user)
                        .build();
                matrixTableRepository.save(matrixTableNew);

                matrixRepository.save(newMatrix);
            });
        }

        return true;
    }


    public List<CloneStateDTO> getCloneStates() {
        List <CloneStat> cloneStats=cloneStatRepository.findAll();
        List <CloneStateDTO> cloneStateDTOS=cloneStats.stream()
                .map(cloneState -> modelMapper.map(cloneState,CloneStateDTO.class))
                .collect(Collectors.toList());
        return cloneStateDTOS;
    }

    public Map getClone (String token,
                         Long matrixType) {

        String login = jwtTokenProvider.getLoginFromToken(token.substring(7));
        Optional<User> user = userRepository.findByUsername(login);
        Optional<MatrixTable> matrixTable = matrixTableRepository.findByUserAndTypeMatrixId(user.get(), matrixType);

        HashMap hashMap=new HashMap();
        hashMap.put("count", matrixTable.get().getCount());
        return hashMap;
    }



}
