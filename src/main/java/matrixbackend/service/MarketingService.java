package matrixbackend.service;

import matrixbackend.entity.Matrix;
import matrixbackend.entity.User;
import matrixbackend.repository.MatrixTypeRepository;
import matrixbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarketingService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatrixTypeRepository matrixTypeRepository;


    public void raiseMoney(List<Matrix> matrices, User user){


        if(matrices.size()==4){
            user.setBalance(user.getBalance()+500);
            userRepository.save(user);
        }
    }


    public void writeOffFromBalance(User user, Long matrix_type) throws Exception {
        int cost=matrixTypeRepository.getById(matrix_type).getSum();

        int currentBalance=user.getBalance();


        if(currentBalance>cost){
            user.setBalance(currentBalance-cost);
            userRepository.save(user);
        }
        else {
            throw new Exception();
        }
    }
}
