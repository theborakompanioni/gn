package gn.geode;

import model.Permission;
import org.springframework.data.gemfire.repository.GemfireRepository;

/**
 * Created by void on 22.09.15.
 */
public interface PermissionGeodeRepository extends GemfireRepository<Permission, String> {
}
