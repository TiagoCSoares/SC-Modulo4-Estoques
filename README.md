# Sistema de Gerenciamento de Estoque
    Santander Coders - Ada Tech
    Módulo Técnicas de Programação-I
    
    #Status: Projeto em desenvolvimento

# Descrição do Projeto

Nossa empresa tem feito o controle de estoque de forma manual a bastante tempo porém está na hora de uma modernização!
As mercadorias em estoque estão todas em um arquivo do tipo CSV, as colunas são ID do produto, nome, quantidade, cateogia, preço. 
Nossa aplicação deve ser capaz de ler este arquivo CSV e gravar em uma tabela de nossa base de dados estas informações.
<br>
<br>

# Funcionalidades

O sistema possui as seguintes funcionalidades:

  - Leitura do arquivo CSV: A aplicação lê o arquivo CSV contendo as informações sobre os produtos em estoque.
  - Relatórios:
    - Quantidade de categorias de produtos.
    - Quantidade de produtos de cada categoria em estoque.
    - Valor médio dos produtos.
  - Identificação de produtos com estoque baixo: Produtos com menos de 3 unidades são listados como estoque baixo.

<br>
<br>

# Tecnologias Utilizadas

- Java
- Banco de Dados MySQL
- Spring Boot com JDBC para conexão com o banco de dados
- Docker Compose para gerenciamento de containers

<br>

## Conceitos

Este projeto foi desenvolvido com o objetivo de aprimorar habilidades em duas áreas principais: o uso de Streams em Java e a integração com banco de dados.

### Streams em Java

As Streams em Java são uma poderosa API introduzida no Java 8 que permite manipular e processar coleções de elementos de forma eficiente e concisa. No contexto deste projeto, as Streams foram utilizadas para processar os dados do arquivo CSV contendo as informações do estoque, permitindo realizar operações como filtragem, mapeamento e redução de forma simples e eficaz.

### Banco de Dados

A integração com banco de dados foi realizada utilizando o MySQL e o framework Spring Boot com JDBC. O MySQL foi escolhido como sistema de gerenciamento de banco de dados relacional devido à sua ampla adoção e robustez. O Spring Boot simplifica o desenvolvimento de aplicações Java, fornecendo uma estrutura pronta para uso e ferramentas que facilitam a integração com o banco de dados, como o Spring JDBC, que simplifica a configuração de conexões e execução de consultas SQL.

<br>
<br>

# Como Executar

1. Faça o download ou clone este repositório.
2. Certifique-se de que você tenha o Docker e o Docker Compose instalados
em sua máquina.
3. No terminal navegue até o diretório raiz do projeto.
4. Execute o seguinte comando para iniciar o banco de dados:

        docker-compose up

    Este comando iniciará um contêiner MySQL utilizando a imagem oficial do MySQL. Ele define as seguintes configurações:

        MYSQL_ALLOW_EMPTY_PASSWORD: Permite uma senha em branco para o usuário root.
        MYSQL_DATABASE: Cria um banco de dados chamado "estoque".
        cap_add: Adiciona a capacidade SYS_NICE ao contêiner (opcional, dependendo dos requisitos da sua aplicação).
        ports: Encaminha a porta 3306 do host para a porta 3306 do contêiner, permitindo acesso ao banco de dados.
5. Execute a classe 

        @SpringBootApplication
        public class EstoquesApplication implements CommandLineRunner

    ou o método 
    
        public void run(String... args) throws Exception 

    Ambos podem ser encontrados em:

        src/main/java/com/example/estoques/EstoquesApplication.java

## Resultados

A execução do código irá seguir o seguinte fluxo:

  - Criar a tabela no banco de dados caso ainda não exista
  - Limpar a tabela caso ela já tenha algo preenchido
  - Ler o arquivo .csv e preencher uma Lista de Produtos com as informações
  - Inserir os dados da lista no banco de dados
  - Gerar os relatórios e exibi-los no terminal
  - Definir produtos com estoque abaixo de 3 itens como estoque baixo
