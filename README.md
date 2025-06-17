# GlucoSmart 🩺📱

GlucoSmart é uma aplicação Android desenvolvida no âmbito da unidade curricular **Interação Pessoa-Computador** da Licenciatura em Engenharia Informática da **ESTIG – Instituto Politécnico de Beja**. 

O seu objetivo é apoiar pessoas com **diabetes tipo 1 e 2** na gestão do consumo de hidratos de carbono (HC), promovendo maior autonomia e segurança alimentar.

## 📌 Funcionalidades

- 🔍 **Pesquisa de Alimentos** com valores nutricionais por 100g  
- ➕ **Adição de Ingredientes** e criação de receitas personalizadas  
- 🧮 **Calculadora de Hidratos de Carbono** baseada na porção ingerida  
- 📖 **Histórico de Refeições** reutilizáveis  
- 📊 **Consulta de Tabelas Nutricionais**  
- 📅 **Gestão de Refeições Diárias**

## 📷 Interface

<p align="center">
  <img src="/screenshots/main_screen.png" width="250"/>
  <img src="/screenshots/calculadora.png" width="250"/>
  <img src="/screenshots/historico.png" width="250"/>
</p>

> As interfaces foram concebidas com foco em **simplicidade, acessibilidade e rapidez**, para abranger utilizadores com diferentes níveis de literacia digital.

## 🧠 Tecnologias Usadas

- `Android Studio`
- `Kotlin` / `Java`
- `XML` para layouts
- `SQLite` para armazenamento local
- `Figma` para protótipos e wireframes

## 👥 Equipa de Desenvolvimento

- **Daniel Raposo** – Criação de refeições personalizadas  
- **Francisco Sequeira** – Calculadora de hidratos de carbono  
- **Paulo Neves** – Histórico de refeições e reutilização  

## 🧪 Avaliação com Utilizadores

Foram realizados testes de usabilidade com base em 3 personas:
- **Mariana** (jovem com literacia digital elevada)
- **João** (pai cuidador e cozinheiro)
- **Sr. Armando** (reformado com baixa literacia digital)

> As sessões foram conduzidas com observação, tarefas orientadas e questionários de pré e pós-teste.

## 📁 Estrutura do Projeto

```bash
GlucoSmart/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
├── screenshots/
├── README.md
└── .gitignore
