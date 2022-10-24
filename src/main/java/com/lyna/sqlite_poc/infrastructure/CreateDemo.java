package com.lyna.sqlite_poc.infrastructure;

import com.lyna.sqlite_poc.infrastructure.repository.DemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateDemo {
    private final DemoRepository demoRepository;

    @Transactional
    public Optional<DemoModel> create(DemoModel demoModel) {
        try {
            if (!demoRepository.existsByName(demoModel.getName())){
                demoModel.setId(null == demoRepository.findMaxId()? 0 : demoRepository.findMaxId() + 1);
                return Optional.of(demoRepository.save(demoModel));
            }
            return Optional.empty();
        }catch (Exception e){
            throw e;
        }
    }
}
