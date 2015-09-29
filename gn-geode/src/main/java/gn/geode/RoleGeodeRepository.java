package gn.geode;

import model.Role;
import org.springframework.data.gemfire.repository.GemfireRepository;

/**
 * Created by void on 22.09.15.
 */
public interface RoleGeodeRepository extends GemfireRepository<Role, String> {
}
