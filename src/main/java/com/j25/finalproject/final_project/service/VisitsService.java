package com.j25.finalproject.final_project.service;

import com.j25.finalproject.final_project.model.Account;
import com.j25.finalproject.final_project.model.Visits;
import com.j25.finalproject.final_project.model.VisitsStatus;
import com.j25.finalproject.final_project.repository.AccountRepository;
import com.j25.finalproject.final_project.repository.VisitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VisitsService {

    @Autowired
    private VisitsRepository visitsRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void addVisit(Visits visit, Principal principal) {
        Optional<Account> doctor = accountRepository.findByUsername(principal.getName());
        if (doctor.isPresent()) {
            Account doctorAccount = doctor.get();

            visit.setDoctor(doctorAccount);
            visit.setStatus(VisitsStatus.TODO);

            visitsRepository.save(visit);
        }
    }

    public Set<Visits> getAllVisitDoctor(String username) {
        Optional<Account> doctor = accountRepository.findByUsername(username);
        return getVisits(doctor);
    }


    public Set<Visits> getAllVisitPatient(String username) {
        Optional<Account> patient = accountRepository.findByUsername(username);
        return getVisitsPatient(patient);
    }

    public Set<Visits> getAllArchived(String username) {
        Optional<Account> doctor = accountRepository.findByUsername(username);
        if (doctor.isPresent()) {
            Account doctorAccount = doctor.get();

            return doctorAccount
                    .getDoctorVisits()
                    .stream()
                    .filter(visit -> visit.getStatus() == VisitsStatus.ARCHIVED)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public Set<Visits> getAllCurrentVisitDoctor(String username) {
        Optional<Account> doctor = accountRepository.findByUsername(username);
        if (doctor.isPresent()) {
            Account doctorAccount = doctor.get();

            return doctorAccount
                    .getDoctorVisits()
                    .stream()
                    .filter(visit -> visit.getStatus() == VisitsStatus.BOOK)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public Set<Visits> getAllArchivedPatient(String username) {
        Optional<Account> patient = accountRepository.findByUsername(username);
        if (patient.isPresent()) {
            Account patientAccount = patient.get();

            return patientAccount
                    .getPatientVisit()
                    .stream()
                    .filter(visit -> visit.getStatus() == VisitsStatus.ARCHIVED)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public void setBook(Long visitId, String patient) {
        Optional<Account> account = accountRepository.findByUsername(patient);
        if (account.isPresent()) {
            Account patientAccount = account.get();
            Optional<Visits> visits = visitsRepository.findById(visitId);
            if (visits.isPresent() && visits.get().getStatus() != VisitsStatus.ARCHIVED) {
                Visits visit = visits.get();
                visit.setStatus(VisitsStatus.BOOK);
                visit.setPatient(patientAccount);

                visitsRepository.save(visit);

            }
        }
    }

    public void setArchive(Long id, String doctor) {
        if (!userIsOwnerOf(doctor, id)) {
            return;
        }

        Optional<Visits> visits = visitsRepository.findById(id);
        if (visits.isPresent() && visits.get().getStatus() == VisitsStatus.BOOK) {
            Visits visit = visits.get();
            visit.setStatus(VisitsStatus.ARCHIVED);

            visitsRepository.save(visit);
        }
    }

    public boolean userIsOwnerOf(String username, Long visitId) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            Account user = account.get();

            return user.getDoctorVisits()
                    .stream()
                    .anyMatch(visit -> visit.getId() == visitId);
        }
        return false;
    }

    public Set<Visits> getAllDoctorVisit(Long id) {
        Optional<Account> doctor = accountRepository.findById(id);
        return getVisits(doctor);
    }

    private Set<Visits> getVisits(Optional<Account> doctor) {
        if (doctor.isPresent()) {
            Account doctorAccount = doctor.get();
            return doctorAccount
                    .getDoctorVisits()
                    .stream()
                    .filter(visit -> visit.getStatus() != VisitsStatus.ARCHIVED)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private Set<Visits> getVisitsPatient(Optional<Account> patient) {
        if (patient.isPresent()) {
            Account patientAccount = patient.get();
            return patientAccount
                    .getPatientVisit()
                    .stream()
                    .filter(visit -> visit.getStatus() != VisitsStatus.ARCHIVED)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    public void removeVisit(Long id) {
        visitsRepository.deleteById(id);
    }

    public Optional<Visits> findByVisitId(Long id) {
        return visitsRepository.findById(id);
    }

//    public Set<Account> getDoctor(Long id) {
//        Optional<Visits> visitsOptional = visitsRepository.findById(id);
//
//        return visitsOptional.get()
//                .getAccountSet()
//                .stream()
//                .filter(d -> d.getAccountRoles().equals("DOCTOR")).collect(Collectors.toSet());
//    }
}
