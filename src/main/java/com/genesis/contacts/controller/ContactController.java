package com.genesis.contacts.controller;

import com.genesis.contacts.domain.Contact;
import com.genesis.contacts.exceptions.ValidationException;
import com.genesis.contacts.service.ContactService;
import com.genesis.contacts.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    ContactService contactService;

    /**
     *
     * @param contact
     * @return
     * @throws ValidationException
     */
    @PostMapping(value = "/add")
    public JsonResponse saveContact(@Valid @RequestBody final Contact contact) throws ValidationException {
        contactService.save(contact);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

    /**
     *
     * @param contact
     * @return
     * @throws ValidationException
     */
    @PutMapping(value = "/edit")
    public JsonResponse editContact(@Valid @RequestBody final Contact contact) throws ValidationException {
        contactService.edit(contact);
        return JsonResponse.builder().data(contact).status(JsonResponse.STATUS.SUCCESS).build();
    }

    /**
     *
     * @param contactId
     * @return
     * @throws ValidationException
     */
    @DeleteMapping(value = "/delete")
    public JsonResponse removeContact(@Valid @RequestParam final Long contactId) throws ValidationException {
        contactService.remove(contactId);
        return JsonResponse.builder().status(JsonResponse.STATUS.SUCCESS).build();
    }

    /**
     *
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public JsonResponse getContacts(final Pageable pageable) {
          return JsonResponse.builder().data(contactService.findAll(pageable))
                .status(JsonResponse.STATUS.SUCCESS).build();
    }

}
