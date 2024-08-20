package grace.guides

import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@Component
@CompileStatic
class BootStrap {

    @Transactional
    void init() {
        (0..8).each {
            new Post(title: "Grace 2022.2.$it is released").save()
        }
        new Post(title: "What's new in Grace 2023.0.0?").save()
    }
}
