# Sicred - Prova Técnica
O objetivo desta aplicação é atender aos requisitos do processo de seleção do Sicred. Esta aplicação consiste em um mecanismo para cadastro de pautas e seções de votação, permitindo ainda o cadastro de associados e o acesso aos resultados de cada votação. 

## Informações Técnicas

Esta aplicação roda em Java 17 com SpringBoot 3, acessando uma base de dados Postgres.
Para executá-la é necessário, alèm da jdk 17, ter o Maven e o Docker instalados localmente.
O banco de dados é gerenciado pelo liquibase que é responsàvel por criar a estrura do banco e gerenciar as mudanças.

## Instruções para montagem da infra

Após instalar o Docker, vá até a pasta "env-starter" localizada na raiz do projeto e execute o comando abaixo:

```batch
docker compose up
```

Após executar o comando acima, acesse o console do PgAdmin para criar um novo server:

```batch
URL: http://localhost:8081
User: admin@sicred.com
Password: admin
```
Após logar, na página inicial acesse a opção Add New Server.

Na aba General, informe um nome aleatòrio e em seguida, na aba Connection, informe os seguintes dados:

```batch
Host name/adress: 172.17.0.1
Username: sicred
Password: sicred
Marque a opção para salvar a senha.
```

## Compilação da aplicação

Para baixar todas as dependências e compilar a aplicação, execute o comando maven a seguir:

```batch
mvn clean install
```

## Excutando a aplicação

Para executar a aplicação, execute o comando maven a seguir:

```batch
mvn spring-boot:run
```

Durante a execuçáo do comando acima, o liquibase se encarregará rodar o script de atualização da estrutura da base.

## Integração com a api externa de validação de CPF

Essa integração foi implementada, porém como esse endpoint só retorna 404 (o tempo todo), criei um parâmetro no arquivo de propriedades da aplicação para desativá-la, se necessário. Por fefault, essa integração fica desabilitada.

```batch
external:
  userInfo:
    url: https://user-info.herokuapp.com
    enabled: false
```

## Documentação Swagger

Para acessar a documentação via swagger, acesse a url abaixo:

```batch
http://localhost:8089/swagger-ui
```

## Algumas Observações

- Alguns pontos poderiam ser implementados para melhoria de performance, por exemplo a criação de chaves compostas nas tabelas de associado e voto. Essas chaves compostas evitariam ter de realizar consultas prévias para saber se o associado já está associado a uma pauta e se ele já votou em determinada pauta. Com as chaves compostas, simplemente trataria o retorno da mensagem caso uma exception de constraint violada fosse lançada. Mas como nao deu tempo, perferi deixar o sistema funcional


- Outra questao importante, também associada a performance, em caso de eolução da aplicação, poderiamos implmentar cache REDIS para algumas entidades com pouca atualização, assim evitaríamos consultas execessivas ä base de dados


- Toda a aplicação foi desenvolvida usando um único controlador de entrada e tudo é feito considerando a entidade pauta como Pai. O que poderia ser feito para melhorar seria isolar o endpoint que registra votos em um módulo à parte, pois será o endpoint mais utilizado e assim poderíamos escaloná-lo de maneira independente.


