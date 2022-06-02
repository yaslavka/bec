package matrixbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import matrixbackend.entity.User;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String locale;
    private String firstName;
    private String middleName;
    private String lastName;
    private String avatar;
    private String phone;
    private String email;
    private String activationDate;
    private String registrationDate;
    private int userOnLink;
    private int partners;
    private int clones;
    private int income;
    private String refLink;
    private String inviter;
    private double balance;
    private int transferBalance;
    private boolean can_create_comment;
    private boolean can_use_school;
    private boolean hasFinPassword;
    private boolean firstEnter;
    private  int activePartners;
    private int firstLine;
    private  int comets;
    private String vk;
    private String instagram;
    private String description;
    private String inviterAvatar;
    private boolean showInviter;
    private String tg;
    private String inviterFio;

    private String username;
    private String MyDescription;
    private String myVk;
    private String myInstagram;
    private String myTg;
    private String tgKey;
    private boolean isAdmin;
    private boolean canReview;

    public UserDTO(User user) {
        this.id = user.getId();
        this.locale = null;
        this.isAdmin=false;
        this.canReview=true;
        this.firstName = user.getFirst_name();
        this.lastName = user.getLast_name();
        this.username=user.getUsername();
        this.hasFinPassword=user.isHasFinPassword();
        this.income=user.getIncome();
        this.firstEnter=user.isFirstEnter();
        this.showInviter=user.isShowInviter();
        this.tgKey=user.getTgKey();
        this.locale=user.getLocale();
        this.can_use_school=true;
        this.hasFinPassword=true;
        this.firstLine=user.getFirstLines();
        this.avatar = Optional.ofNullable(user.getAvatar()).orElse("");
        this.registrationDate =  user.getRegistrationDate().toString();
        this.refLink =Optional.ofNullable(user.getRefLink()).orElse("");
        Optional.ofNullable(user.getReferral()).ifPresent(inviter->{
            setInviter(inviter.getUsername());
            setInviterAvatar(inviter.getAvatar());
            setInviterFio(inviter.getUsername());
            setTg(inviter.getTelegram());
            setInstagram(inviter.getInstagram());
            setVk(inviter.getVkontakte());
        });
        Optional.ofNullable(user.getActivationDate()).ifPresent(activationDate->{
            setActivationDate(user.getActivationDate().toString());
        });
        this.income=user.getIncome();
        this.balance = user.getBalance();
        this.myVk = user.getVkontakte();
        this.myInstagram = user.getInstagram();
        this.myTg = user.getTelegram();
        this.activePartners=user.getActivePartners();
        this.middleName="";
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", locale='" + locale + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", activationDate='" + activationDate + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", userOnLink=" + userOnLink +
                ", partners=" + partners +
                ", income=" + income +
                ", refLink='" + refLink + '\'' +
                ", inviter='" + inviter + '\'' +
                ", balance=" + balance +
                ", transferBalance=" + transferBalance +
                ", can_create_comment=" + can_create_comment +
                ", can_use_school=" + can_use_school +
                ", hasFinPassword=" + hasFinPassword +
                ", firstEnter=" + firstEnter +
                ", activePartners=" + activePartners +
                ", firstLine=" + firstLine +
                ", comets=" + comets +
                ", vk='" + vk + '\'' +
                ", tg='" + tg + '\'' +
                ", instagram='" + instagram + '\'' +
                ", description='" + description + '\'' +
                ", inviterAvatar='" + inviterAvatar + '\'' +
                ", showInviter=" + showInviter +
                ", inviterFio='" + inviterFio + '\'' +
                ", myVk='" + myVk + '\'' +
                ", myInstagram='" + myInstagram + '\'' +
                ", myTg='" + myTg + '\'' +
                ", tgKey='" + tgKey + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
