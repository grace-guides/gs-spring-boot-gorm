package grace.guides

import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

@Component
@CompileStatic
class BootStrap {

    @Transactional
    void init() {
        new Post(title: 'Grace 2022.2.0 is released').save()
        new Post(title: 'Grace 2022.2.1 is released').save()
        new Post(title: 'Grace 2022.2.2 is released').save()
        new Post(title: 'Grace 2022.2.3 is released').save()
        new Post(title: 'Grace 2022.2.4 is released').save()
        new Post(title: 'Grace 2022.2.5 is released').save()
        new Post(title: "What's new in Grace 2023.0.0?").save()
    }
}
