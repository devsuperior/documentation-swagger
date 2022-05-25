# Documentação de API com Swagger no Spring Boot com Java
>  *Criar uma documentação para o projeto Dsmovie utilizando o Swagger*

## Aula 1 - Introduçao ao swagger

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


## Aula 2 - Recursos avançados no Swagger

### Passso: Personalizar o swagger

- Anotações nos recursos (controllers)

```java
@Api(tags = "Movie Controller", value = "MovieController", description = "Controller for Movie")
public class MovieController {
```
- Anotações nos endpoints REST

```java
@PostMapping
@ApiOperation(value = "Create a new movie")
@ApiResponses(value = {
	@ApiResponse(code = 200, message = "Product inserted successfully"),
	@ApiResponse(code = 400, message = "Bad Request")
})
public ResponseEntity<MovieDTO> insert(@RequestBody MovieDTO dto) {
```

```java
@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
public MovieDTO findById(@PathVariable Long id) {
```

- Anotações model

```java
public class MovieDTO {

	@ApiModelProperty(notes = "Database generated movie ID")
	private Long id;
	
	@ApiModelProperty(notes = "Movie title")
	private String title;
```
- Incluindo metadados swagger

```java
@Bean
public Docket api() {
	return new Docket(DocumentationType.SWAGGER_2).select()
		.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
		.build()
		.apiInfo(metaData());
}

private ApiInfo metaData() {
	return new ApiInfoBuilder().title("Dsmovie API")
		.description("\"Spring Boot REST API for SDS 8\"").version("1.0.0")
		.contact(new Contact("Devsuperior", "https://github.com/devsuperior", "https://www.instagram.com/devsuperior.ig/"))
		.build();
}
```

### Passso: Bean validation

- Ajuste no arquivo pom.xml:

```xml
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
</dependency>

<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```

- Validações básicas

```java
public class MovieDTO {

	@NotEmpty(message = "can't be empty")
	@Length(min = 3, max = 50, message = "Length must be between 5 and 80")
	private String title;
	
	@PositiveOrZero
	private Double score;

	...
```

```java
public class ScoreDTO {
	
	@Email
	private String email;
	
	...

```

```java
public class MovieController {

	@PostMapping
	public ResponseEntity<MovieDTO> insert(@Valid @RequestBody MovieDTO dto)
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<MovieDTO> update(@PathVariable Long id, @Valid @RequestBody MovieDTO dto)
	
	...
```

```java
public class MovieController {

	@PutMapping
	public MovieDTO saveScore(@Valid @RequestBody ScoreDTO dto)
	
	...
```


