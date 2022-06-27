package by.itacademy.zuevvlad.cardpaymentproject.controller.rest.client;

import by.itacademy.zuevvlad.cardpaymentproject.dto.DTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Entity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

public abstract class RestClient<TypeOfEntity extends Entity, TypeOfDTO extends DTO>
{
    private final RestTemplate restTemplate;
    private final String url;
    private final Class<TypeOfDTO> typeOfDTO;

    protected RestClient(final RestTemplate restTemplate, final String url, final Class<TypeOfDTO> typeOfDTO)
    {
        super();
        this.restTemplate = restTemplate;
        this.url = url;
        this.typeOfDTO = typeOfDTO;
    }

    protected final RestTemplate getRestTemplate()
    {
        return this.restTemplate;
    }

    protected final String getUrl()
    {
        return this.url;
    }

    public final Collection<TypeOfDTO> findAll()
    {
        final ParameterizedTypeReference<Collection<TypeOfDTO>> parameterizedTypeReference
                = new ParameterizedTypeReference<Collection<TypeOfDTO>>(){};
        final ResponseEntity<Collection<TypeOfDTO>> responseEntity = this.restTemplate.exchange(this.url,
                HttpMethod.GET, RequestEntity.EMPTY, parameterizedTypeReference);
        return responseEntity.getBody();
    }

    public final TypeOfDTO findById(final long idOfFoundEntity)
    {
        final String urlToFindEntityById = this.url + "/" + idOfFoundEntity;
        final ParameterizedTypeReference<TypeOfDTO> parameterizedTypeReference
                = new ParameterizedTypeReference<TypeOfDTO>(){};
        final ResponseEntity<TypeOfDTO> responseEntity = this.restTemplate.exchange(urlToFindEntityById, HttpMethod.GET,
                RequestEntity.EMPTY, parameterizedTypeReference);
        return responseEntity.getBody();
    }

    public TypeOfDTO add(final TypeOfEntity addedEntity)
    {
        final ResponseEntity<TypeOfDTO> responseEntity = this.restTemplate.postForEntity(this.url, addedEntity,
                this.typeOfDTO);
        return responseEntity.getBody();
    }

    public void update(final TypeOfEntity updatedEntity)
    {
        this.restTemplate.put(this.url, updatedEntity);
    }

    public final void delete(final long idOfDeletedEntity)
    {
        final String urlToDeleteEntity = this.url + "/" + idOfDeletedEntity;
        this.restTemplate.delete(urlToDeleteEntity);
    }
}
