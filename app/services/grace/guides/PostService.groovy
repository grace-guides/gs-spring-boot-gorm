package grace.guides

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import grails.gorm.services.Service
import groovy.transform.CompileStatic

@CompileStatic
@Service(Post)
interface PostService {

    List<Post> findAll()

    Post save(Post post)

}
