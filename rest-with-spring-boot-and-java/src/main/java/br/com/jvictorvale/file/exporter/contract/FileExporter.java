package br.com.jvictorvale.file.exporter.contract;

import br.com.jvictorvale.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}
