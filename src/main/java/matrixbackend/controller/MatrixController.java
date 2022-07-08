package matrixbackend.controller;

import matrixbackend.dto.*;
import matrixbackend.entity.Matrix;
import matrixbackend.repository.CloneStatRepository;
import matrixbackend.repository.MatrixRepository;
import matrixbackend.service.MatrixService;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping(value = "/matrix")
public class MatrixController {

    @Autowired
    MatrixService matrixService;

    @Autowired
    CloneStatRepository cloneStatRepository;

    @Autowired
    MatrixRepository matrixRepository;



    @Autowired
    ModelMapper modelMapper;

    @GetMapping(value = "/type")
    public ResponseEntity getAllMatrixType(
            @RequestHeader(name="Authorization") String token,
            @QuerydslPredicate(root = Matrix.class) Predicate predicate){
        HashMap hashMap=new HashMap();
        List <MatrixTypeDTO> list =matrixService.getAllMatrixType(token);
        hashMap.put("items",list);
            return ResponseEntity.ok(hashMap);
    }



    @GetMapping("/structure")
    public ResponseEntity getAllMatrix(
            @RequestParam(required = false)  Long matrix_type,
            @RequestParam(required = false) Long matrix_id,
            @RequestHeader(name="Authorization") String token,
            @QuerydslPredicate(root = Matrix.class) Predicate predicate) throws Exception {
        HashMap map =new HashMap();
        map.put("items",matrixService.getAllActivatedMatrices(matrix_id,token,matrix_type));
        return ResponseEntity.ok(map);
    }

    @PostMapping("/buy")
    public ResponseEntity buyNewMatrix(@RequestHeader(name="Authorization") String token,
                                          @RequestBody BuyMatrixDTO  matrix) throws Exception {
        HashMap hashMap=new HashMap();
        if (matrixService.createNewMatrix(token,matrix.getMatrix_id()))
            hashMap.put("status","success");
        else {
            hashMap.put("status", "error");
            return ResponseEntity.status(406).build();
        }
        return ResponseEntity.ok(hashMap);
    }


    @GetMapping("/clone-stat")
    public ResponseEntity getCloneStat(){
        JSONObject jsonObject= new JSONObject();
        List<CloneStateDTO> cloneStateDTOS=matrixService.getCloneStates();
        jsonObject.put("items",cloneStateDTOS);
        return ResponseEntity.ok(jsonObject);
    }

    @PostMapping(value = "/target-install-clone")
    public ResponseEntity installCloneToBoard(@RequestHeader(name="Authorization") String token,
                                              @RequestBody InstallCloneRequestDTO installCloneRequestDTO){
        if (!matrixService.installMatrixToBinaryTree(token,installCloneRequestDTO))
            return ResponseEntity.status(406).build(); //Если установка клона пошла не по плану, кидаем ошибку
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dash-info")
    public ResponseEntity<Map> getMatrixType(@RequestParam Long matrix_type,
                                             @QuerydslPredicate(root = Matrix.class) Predicate predicate,
                                             @RequestHeader(name="Authorization") String token){
        HashMap hashMap=new HashMap();
        List<CloneDTO> cloneDTOList=matrixService.getAllClones(token,predicate,matrix_type);
        hashMap.put("items",cloneDTOList);
        return ResponseEntity.ok(hashMap);
    }

    @GetMapping("/clone")
    public ResponseEntity<Map> getCloneCount(@RequestParam Long matrix_type,
                                             @RequestHeader(name="Authorization") String token){
        //HashMap hashMap=new HashMap();
        //hashMap.put("count",0);

        HashMap hashMap = (HashMap) matrixService.getClone(token, matrix_type);

        //System.out.println(hashMap);

        return ResponseEntity.ok(hashMap);
    }

    @GetMapping("/structure-upper")
    public ResponseEntity getParentMatrix (@RequestParam Long matrix_id,
                                                @RequestHeader(name="Authorization") String token) throws Exception {

        HashMap map =new HashMap();
        map.put("items",matrixService.getAllActivatedMatrices(matrixRepository.findById(matrix_id).get().getParentMatrix().getId(),token, null));
        return ResponseEntity.ok(map);
    }
}
