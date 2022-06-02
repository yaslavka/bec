package matrixbackend.service;

import matrixbackend.entity.User;
import matrixbackend.jwt.JwtTokenProvider;
import matrixbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class StorageService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    Environment environment;






    private final String uploadDir ="/getFile/avatar/";
    public  void store(MultipartFile file,String token) throws IOException {
        String extractLoginFromToken= jwtTokenProvider.getLoginFromToken(token.substring(7));
        UUID uuid = UUID.randomUUID();
        String uuidWithExtension=uuid+getExtensionByStringHandling(file.getOriginalFilename());
        FileOutputStream output = new FileOutputStream(new File("src/main/getFile/avatar")
                .getAbsolutePath() + "/" + uuidWithExtension);
        Optional<User> userExist=userRepository.findByUsername(extractLoginFromToken);
        userExist.get().setAvatar(uploadDir+uuidWithExtension);
        userRepository.save(userExist.get());
        output.write(file.getBytes());
        output.close();
    }




    public String getExtensionByStringHandling(String filename) {
       String extractedExtension= Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1)).get();
        return new String("."+extractedExtension);
    }

}
