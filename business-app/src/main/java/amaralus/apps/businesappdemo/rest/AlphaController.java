package amaralus.apps.businesappdemo.rest;

import amaralus.apps.businesappdemo.entities.Alpha;
import amaralus.apps.businesappdemo.services.CrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("alpha")
public class AlphaController extends CrudController<Alpha> {

    public AlphaController(CrudService<Alpha, String> crudService) {
        super(crudService);
    }
}
