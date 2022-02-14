package com.genesis.contacts.controller;

import com.genesis.contacts.domain.Contact;
import com.genesis.contacts.domain.Entreprise;
import com.genesis.contacts.exceptions.ValidationException;
import com.genesis.contacts.service.EntrepriseService;
import com.genesis.contacts.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/entreprise")
public class EntrepriseController {
    @Autowired
    EntrepriseService entrepriseService;

    /**
     *
     * @param entreprise
     * @return
     * @throws ValidationException
     */
    @PostMapping(value = "/add")
    public JsonResponse saveEntreprise(@Valid @RequestBody final Entreprise entreprise) throws ValidationException {
        entrepriseService.save(entreprise);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

    /**
     *
     * @param entreprise
     * @return
     * @throws ValidationException
     */
    // @Hidden
    @PutMapping(value = "/edit")
    public JsonResponse editEntreprise(@Valid @RequestBody final Entreprise entreprise) throws ValidationException {
        entrepriseService.edit(entreprise);
        return JsonResponse.builder().data(entreprise).status(JsonResponse.STATUS.SUCCESS).build();
    }


    /**
     *
     * @param entrepriseId
     * @param contactId
     * @param contactType
     * @return
     * @throws ValidationException
     */
    @PostMapping(value = "/addContact")
    public JsonResponse addContactToEntreprise(@Valid @RequestParam final Long entrepriseId,
                                               @RequestParam final Long contactId,
                                               @RequestParam final String contactType)
        throws ValidationException {
        entrepriseService.addContact(entrepriseId,contactId, contactType);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

    /**
     *
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public JsonResponse getEntreprises(final Pageable pageable) {
        return JsonResponse.builder().data(entrepriseService.findAll(pageable))
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

    /**
     *
     * @param tvaEntreprise
     * @return
     */
    @RequestMapping(value = "/findByTva", method = RequestMethod.GET)
    public JsonResponse findByTva(@RequestParam final String tvaEntreprise) {
       Entreprise entreprise= entrepriseService.findByTvaEntreprise(tvaEntreprise);
        return JsonResponse.builder().data(entreprise)
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

}
