package grace.guides

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import org.springframework.stereotype.Service

@CompileStatic
@grails.gorm.services.Service(Post)
@Service
interface PostService {

    List<Post> findAll()

    Post save(Post post)

}
