package com.spring.baseproject.modules.rbac.models.entities;

import com.spring.baseproject.modules.auth.models.entities.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "api")
public class Api {
    public static final String METHOD = "method";
    public static final String API_FUNCTION = "apiFunction";
    public static final String ROLES = "roles";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "role_api",
            joinColumns = @JoinColumn(name = "api_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method")
    private ApiMethod method;
    @Column(name = "route")
    private String route;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "api_function")
    private ApiFunction apiFunction;

    public Api() {

    }

    public Api(ApiMethod method, String route) {
        setMethod(method);
        setRoute(route);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles.clear();
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public ApiMethod getMethod() {
        return method;
    }

    public void setMethod(ApiMethod method) {
        this.method = method;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (this.description == null || !this.description.equals(description)) {
            this.description = description;
        }
    }

    public ApiFunction getApiFunction() {
        return apiFunction;
    }

    public void setApiFunction(ApiFunction apiFunction) {
        this.apiFunction = apiFunction;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Api && ((Api) obj).id == this.id;
    }
}
