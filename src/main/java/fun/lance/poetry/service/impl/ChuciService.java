package fun.lance.poetry.service.impl;

import fun.lance.poetry.service.IPoetryService;
import org.springframework.stereotype.Service;

@Service
public class ChuciService implements IPoetryService {
    @Override
    public String getName() {
        return "楚辞";
    }

    @Override
    public void readAndUpload(String path) {

    }
}
