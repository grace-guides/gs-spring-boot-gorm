package grace.guides

import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@Component
@CompileStatic
class BootStrap {

    @Transactional
    void init() {
        (0..9).each {
            new Post(title: "Grace 2022.2.$it is released").save()
        }
        new Post(title: "What's new in Grace 2023.0.0?").save()
        new Post(title: "Grace 2023.1.0 is also available now!").save()
        new Comment(text: "Wow, Grace is awesome!").save()
    }
}
