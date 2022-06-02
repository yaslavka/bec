package matrixbackend.controller;

import matrixbackend.dto.*;
import matrixbackend.entity.Matrix;
import matrixbackend.repository.CloneStatRepository;
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
    ModelMapper modelMapper;

    @GetMapping(value = "/type")
    public ResponseEntity getAllMatrixType(
            @QuerydslPredicate(root = Matrix.class) Predicate predicate){
        HashMap hashMap=new HashMap();
        List <MatrixTypeDTO> list =matrixService.getAllMatrixType();
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
        matrixService.createNewMatrix(token,matrix.getMatrix_id());
        hashMap.put("status","success");
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
        matrixService.installMatrixToBinaryTree(token,installCloneRequestDTO);
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
    public ResponseEntity<Map> getCloneCount(@RequestParam Long matrix_type){
        HashMap hashMap=new HashMap();
        hashMap.put("count",0);
        return ResponseEntity.ok(hashMap);
    }
}
