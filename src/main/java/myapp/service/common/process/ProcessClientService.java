package myapp.service.common.process;

import lombok.RequiredArgsConstructor;
import myapp.domain.Client;
import myapp.repository.ClientRepository;
import myapp.service.common.ProcessMessageService;
import myapp.service.dto.ClientDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ProcessClientService implements ProcessMessageService<ClientDto> {
    private final ClientRepository clientRepository;
    @Override
    public void process(ClientDto clientDto){
        //логика обработки
        Client client = new Client();
        clientRepository.save(client);
    }
}
