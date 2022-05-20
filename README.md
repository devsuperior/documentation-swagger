# Documentação de API com Swagger no Spring Boot com Java
>  *Criar uma documentação para o projeto Dsmovie utilizando o Swagger*

## Aula 1

### Passso: incluir novos endpoints


```java
@Service
public class MovieService {

  @Autowired
	private MovieRepository repository;
  
  ...

  @Transactional
  public MovieDTO insert(MovieDTO dto) {
      Movie entity = dto.toEntity();
      entity = repository.save(entity);
      return new MovieDTO(entity);
  }
  
  @Transactional
  public MovieDTO update(Long id, MovieDTO dto) {
      Movie entity = repository.getById(id);
      updateData(entity, dto);
      entity = repository.save(entity);
      return new MovieDTO(entity);
  }
  
  public void delete(Long id) {
      repository.deleteById(id);
  }

  private void updateData(Movie entity, MovieDTO dto) {
      entity.setTitle(dto.getTitle());
      entity.setScore(dto.getScore());
      entity.setCount(dto.getCount());
      entity.setImage(dto.getImage());
  }
```

```java
@RestController
@RequestMapping(value = "/movies")
public class MovieController {

  @Autowired
	private MovieService service;
  
  ...

  @PostMapping
  public ResponseEntity<MovieDTO> insert(@RequestBody MovieDTO dto) {
      dto = service.insert(dto);
      URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
          .buildAndExpand(dto.getId()).toUri();
      return ResponseEntity.created(uri).body(dto);
  }
  
  @PutMapping(value = "/{id}")
  public ResponseEntity<MovieDTO> update(@PathVariable Long id, @RequestBody MovieDTO dto) {
      dto = service.update(id, dto);
      return ResponseEntity.ok().body(dto);
  }
  
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<MovieDTO> delete(@PathVariable Long id) {
      service.delete(id);
      return ResponseEntity.noContent().build();
  }
```

### Passo: baixar o projeto e incluir as dependências do Maven

- Clonar o projeto Spring Boot Dsmovie (* para este projeto vamos usar somente o backend)

- Ajuste no arquivo pom.xml:

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>3.0.0</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

### Passo: classe de configuração

- Incluir a classe SwaggerConfig no pacote config:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }
}
```

- Configuração no application.properties para a versao mais recente do Swagger

```

spring.mvc.pathmatch.matching-strategy=ant-path-matcher

```

### Passo: acesso ao swagger

- Link: http://localhost:8080/swagger-ui.html



