package info.alarmap.us.alarmap._models;

import java.util.ArrayList;

/**
 * Created by julianperez on 6/19/17.
 */

public class Incidence {
    public String id;
    public String typeUser;
    public String companyType;
    public String incidenDesctiption;
    public Post localization;
    public String incidenImage;


    public  Incidence() {
    }

    public Incidence(String id,

                      String typeUser,
                      String companyType,
                      String incidenDesctiption,
                      Post localization,
                      String incidenImage
    ) {
        this.id = id;
        this.typeUser = typeUser;
        this.companyType = companyType;
        this.incidenDesctiption = incidenDesctiption;
        this.localization = localization;
        this.incidenImage = incidenImage;
    }

    public Incidence(String typeUser,
                     String companyType,
                     String incidenDesctiption,
                     Post localization,
                     String incidenImage
    ) {
        this.typeUser = typeUser;
        this.companyType = companyType;
        this.incidenDesctiption = incidenDesctiption;
        this.localization = localization;
        this.incidenImage = incidenImage;
    }
}
