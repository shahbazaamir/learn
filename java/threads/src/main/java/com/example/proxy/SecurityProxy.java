package com.example.proxy;


/**
 * SecurityProxy is a protection proxy that enforces role-based access control
 * before delegating calls to the underlying DatabaseService.
 *
 * <p>Roles: USER (read-only), ADMIN (read + write)</p>
 */
public class SecurityProxy implements DatabaseService {
    private final DatabaseService target;
    private final String userRole;

    /**
     * @param target   the real service to delegate to
     * @param userRole the role of the current user (USER or ADMIN)
     */

    public SecurityProxy(DatabaseService target, String userRole) {
        this.target = target;
        this.userRole = userRole;
    }
    

    @Override
    public String getData(String query) {
        if (!hasReadAccess()) {
            throw new SecurityException("Access denied: insufficient permissions");
        }
        System.out.println("Security check passed for read operation");
        return target.getData(query);
    }
    

    @Override
    public void saveData(String data) {
        if (!hasWriteAccess()) {
            throw new SecurityException("Access denied: write permission required");
        }
        System.out.println("Security check passed for write operation");
        target.saveData(data);
    }
    
    private boolean hasReadAccess() {
        return "USER".equals(userRole) || "ADMIN".equals(userRole);
    }
    
    private boolean hasWriteAccess() {
        return "ADMIN".equals(userRole);
    }
}