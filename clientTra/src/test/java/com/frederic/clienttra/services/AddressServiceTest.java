package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.AddressNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.exceptions.LastAddressException;
import com.frederic.clienttra.mappers.AddressMapper;
import com.frederic.clienttra.repositories.AddressRepository;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.validators.AddressValidator;
import com.frederic.clienttra.validators.DtoValidator;
import com.frederic.clienttra.validators.OwnerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private OwnerValidator ownerValidator;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private DtoValidator dtoValidator;
    @Mock
    private AddressValidator addressValidator;
    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- getAllAddresses ---
    @Test
    void getAllAddresses_shouldReturnSortedAddressDTOList() {
        int idCompany = 1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Address a1 = new Address();
        a1.setCity("Barcelona");
        Address a2 = new Address();
        a2.setCity("Madrid");
        Address a3 = new Address();
        a3.setCity(null);
        List<Address> entities = Arrays.asList(a2, a3, a1);

        when(addressRepository.findByCompany_IdCompany(idCompany)).thenReturn(entities);

        AddressDTO dto1 = new AddressDTO();
        dto1.setCity("Barcelona");
        AddressDTO dto2 = new AddressDTO();
        dto2.setCity("Madrid");
        AddressDTO dto3 = new AddressDTO();
        dto3.setCity(null);
        List<AddressDTO> dtos = Arrays.asList(dto2,dto3,dto1);

        when(addressMapper.toAddressDTOList(entities)).thenReturn(dtos); // simulo conversion

        // El método retorna lista ordenada por city (ignora nulls)
        List<AddressDTO> result = addressService.getAllAddresses(idCompany);

        // Verifico llamada a checkOwner
        verify(ownerValidator).checkOwner(idCompany);
        verify(addressRepository).findByCompany_IdCompany(idCompany);
        verify(addressMapper).toAddressDTOList(entities);

        // La lista debe tener el tamaño correcto
        assertThat(result).hasSize(3);

        // Comprobamos que está ordenada por ciudad (ignorando null al final)
        assertThat(result.get(0).getCity()).isEqualTo("Barcelona");
        assertThat(result.get(1).getCity()).isEqualTo("Madrid");
        assertThat(result.get(2).getCity()).isNull();
    }

    // --- getAddress ---
    @Test
    void getAddress_shouldReturnAddressDTO_whenFound() {
        int idCompany = 1;
        int idAddress = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Address entity = new Address();
        when(addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany))
                .thenReturn(Optional.of(entity));

        AddressDTO dto = new AddressDTO();
        when(addressMapper.toAddressDTO(entity)).thenReturn(dto);

        AddressDTO result = addressService.getAddress(idCompany, idAddress);

        verify(ownerValidator).checkOwner(idCompany);
        verify(addressRepository).findByIdAddressAndCompany_idCompany(idAddress, idCompany);
        verify(addressMapper).toAddressDTO(entity);

        assertThat(result).isSameAs(dto);
    }

    @Test
    void getAddress_shouldThrowAddressNotFoundException_whenNotFound() {
        int idCompany = 1;
        int idAddress = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.getAddress(idCompany, idAddress))
                .isInstanceOf(AddressNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(addressRepository).findByIdAddressAndCompany_idCompany(idAddress, idCompany);
        verify(addressMapper, never()).toAddressDTO(any());
    }

    // --- deleteAddress ---
    @Test
    void deleteAddress_shouldDelete_whenNotLastAddress() {
        int idCompany = 1;
        int idAddress = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Address entity = new Address();
        when(addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany))
                .thenReturn(Optional.of(entity));

        when(addressValidator.isLastAddress(idCompany)).thenReturn(false);

        addressService.deleteAddress(idCompany, idAddress);

        verify(ownerValidator).checkOwner(idCompany);
        verify(addressRepository).findByIdAddressAndCompany_idCompany(idAddress, idCompany);
        verify(addressValidator).isLastAddress(idCompany);
        verify(addressRepository).delete(entity);
    }

    @Test
    void deleteAddress_shouldThrowLastAddressException_whenIsLast() {
        int idCompany = 1;
        int idAddress = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Address entity = new Address();
        when(addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany))
                .thenReturn(Optional.of(entity));

        when(addressValidator.isLastAddress(idCompany)).thenReturn(true);

        assertThatThrownBy(() -> addressService.deleteAddress(idCompany, idAddress))
                .isInstanceOf(LastAddressException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(addressRepository).findByIdAddressAndCompany_idCompany(idAddress, idCompany);
        verify(addressValidator).isLastAddress(idCompany);
        verify(addressRepository, never()).delete(any());
    }

    @Test
    void deleteAddress_shouldThrowAddressNotFoundException_whenAddressNotFound() {
        int idCompany = 1;
        int idAddress = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.deleteAddress(idCompany, idAddress))
                .isInstanceOf(AddressNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(addressRepository).findByIdAddressAndCompany_idCompany(idAddress, idCompany);
        verify(addressValidator, never()).isLastAddress(anyInt());
        verify(addressRepository, never()).delete(any());
    }

    // --- createAddress ---
    @Test
    void createAddress_shouldSaveAddress_whenCompanyExists() {
        int idCompany = 1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Company company = new Company();
        when(companyRepository.findByIdCompany(idCompany)).thenReturn(Optional.of(company));

        CreateAddressRequestDTO dto = new CreateAddressRequestDTO();

        Address entity = new Address();
        when(addressMapper.toEntity(dto)).thenReturn(entity);

        addressService.createAddress(idCompany, dto);

        verify(ownerValidator).checkOwner(idCompany);
        verify(companyRepository).findByIdCompany(idCompany);
        verify(addressMapper).toEntity(dto);
        // El entity debe tener la compañía seteada
        assertThat(entity.getCompany()).isSameAs(company);
        verify(addressRepository).save(entity);
    }

    @Test
    void createAddress_shouldThrowCompanyNotFoundException_whenCompanyNotFound() {
        int idCompany = 1;
        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(companyRepository.findByIdCompany(idCompany)).thenReturn(Optional.empty());

        CreateAddressRequestDTO dto = new CreateAddressRequestDTO();

        assertThatThrownBy(() -> addressService.createAddress(idCompany, dto))
                .isInstanceOf(CompanyNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(companyRepository).findByIdCompany(idCompany);
        verify(addressMapper, never()).toEntity(any());
        verify(addressRepository, never()).save(any());
    }

    // --- updateAddress ---
    @Test
    void updateAddress_shouldUpdateAndReturnDTO_whenAddressFound() {
        int idCompany = 1;
        int idAddress = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        Address entity = new Address();
        when(addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany))
                .thenReturn(Optional.of(entity));

        UpdateAddressRequestDTO updateDto = new UpdateAddressRequestDTO();
        CreateAddressRequestDTO createDto = new CreateAddressRequestDTO();

        when(addressMapper.toCreateAddressRequestDTO(updateDto, entity)).thenReturn(createDto);

        // dtoValidator no devuelve nada, asumo que no lanza excepción
        doNothing().when(dtoValidator).validate(createDto);

        doAnswer(invocation -> {
            // Simula actualización de entidad
            return null;
        }).when(addressMapper).updateEntity(entity, updateDto);

        Address updatedEntity = new Address();
        when(addressRepository.save(entity)).thenReturn(updatedEntity);

        AddressDTO resultDto = new AddressDTO();
        when(addressMapper.toAddressDTO(updatedEntity)).thenReturn(resultDto);

        AddressDTO result = addressService.updateAddress(idCompany, idAddress, updateDto);

        verify(ownerValidator).checkOwner(idCompany);
        verify(addressRepository).findByIdAddressAndCompany_idCompany(idAddress, idCompany);
        verify(addressMapper).toCreateAddressRequestDTO(updateDto, entity);
        verify(dtoValidator).validate(createDto);
        verify(addressMapper).updateEntity(entity, updateDto);
        verify(addressRepository).save(entity);
        verify(addressMapper).toAddressDTO(updatedEntity);

        assertThat(result).isSameAs(resultDto);
    }

    @Test
    void updateAddress_shouldThrowAddressNotFoundException_whenNotFound() {
        int idCompany = 1;
        int idAddress = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(addressRepository.findByIdAddressAndCompany_idCompany(idAddress, idCompany)).thenReturn(Optional.empty());

        UpdateAddressRequestDTO updateDto = new UpdateAddressRequestDTO();

        assertThatThrownBy(() -> addressService.updateAddress(idCompany, idAddress, updateDto))
                .isInstanceOf(AddressNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(addressRepository).findByIdAddressAndCompany_idCompany(idAddress, idCompany);
        verify(addressMapper, never()).toCreateAddressRequestDTO(any(), any());
        verify(dtoValidator, never()).validate(any());
        verify(addressMapper, never()).updateEntity(any(), any());
        verify(addressRepository, never()).save(any());
        verify(addressMapper, never()).toAddressDTO(any());
    }
}
