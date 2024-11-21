package grace.guides

import grails.persistence.Entity

@Entity
class Comment implements Serializable {

    String text

    static constraints = {
        text(blank: false, nullable: false, maxSize:255)
    }

}
