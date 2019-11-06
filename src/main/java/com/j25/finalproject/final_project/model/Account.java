package com.j25.finalproject.final_project.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 4)
    private String username;

    @NotEmpty
    @Size(min = 4)
    private String password;

    private String title;
    private String name;
    private String surname;

//    @Size(min = 11, max = 11)
    private String pesel;

    private String street;
    private String homeNumber;
    private String postcode;
    private String city;
    private Nationality nationality;

    private String phoneNumber;

    private String emailAddress;

    private String offceNumber;

    private Specialization specialization;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private Set<Visits> doctorVisits;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private Set<Visits> patientVisit;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Cascade(value = org.hibernate.annotations.CascadeType.DETACH)
    private Set<AccountRole> accountRoles;

    private boolean locked;

    public boolean isAdmin() {
        return accountRoles.stream()
                .anyMatch(accountRole -> accountRole.getName().equals("ADMIN"));
    }

    public Account(@NotEmpty @Size(min = 4) String username, @NotEmpty @Size(min = 4) String password, String name,
                   String surname, @Size(min = 11, max = 11) String pesel, String street, String homeNumber,
                   String postcode, String city, Nationality nationality, String phoneNumber, String emailAddress) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.street = street;
        this.homeNumber = homeNumber;
        this.postcode = postcode;
        this.city = city;
        this.nationality = nationality;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }
}
