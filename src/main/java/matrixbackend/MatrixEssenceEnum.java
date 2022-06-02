package matrixbackend;

public enum MatrixEssenceEnum {
    Original((short)0),
    Clone((short)1);


    short value;

    MatrixEssenceEnum(short i) {
        this.value  = i;
    }

}

