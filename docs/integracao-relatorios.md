# Contrato de Integração do Módulo de Relatórios

## Objetivo

Definir os dados necessários para integrar os módulos de prontuário médico e controle de vacinas aos relatórios individuais e institucionais.

---

## Módulo de Vacinas

A entidade `Vacina` deve possuir, no mínimo:

- `id`
- `paciente`
- `identificacaoVacina`
- `dataAplicacao`

O módulo deve permitir:

- listar vacinas aplicadas em uma paciente;
- buscar registros por identificação da vacina;
- calcular quantas pacientes distintas receberam determinada vacina.

### Métodos esperados no repositório ou service

```java
List<Vacina> findByPacienteOrderByDataAplicacaoDesc(
        Paciente paciente
);
```

```java
List<Vacina> findByIdentificacaoVacinaIgnoreCase(
        String identificacaoVacina
);
```

Também pode ser disponibilizado um método no service:

```java
List<Vacina> buscarVacinasPorPaciente(
        Paciente paciente
);
```

---

## Módulo de Prontuário Médico

A entidade `ProntuarioMedico` deve possuir, no mínimo:

- `id`
- `paciente`
- lista de consultas
- lista de prescrições
- lista de exames
- histórico de internações
- histórico de vacinação

O módulo deve permitir:

- localizar o prontuário de uma paciente;
- adicionar consultas;
- listar prescrições vinculadas;
- vincular resultados de exames;
- gerar um resumo do histórico;
- buscar consultas por data ou profissional.

### Métodos esperados no repositório ou service

```java
Optional<ProntuarioMedico> findByPaciente(
        Paciente paciente
);
```

Também pode ser disponibilizado um método no service:

```java
ProntuarioMedico buscarProntuarioPorPaciente(
        Paciente paciente
);
```

---

## Módulo de Prescrições

O relatório individual precisa exibir os medicamentos que a paciente está utilizando. Para isso, o módulo de prontuário deve disponibilizar os dados das prescrições associadas à paciente.

A entidade `Prescricao` deve possuir, no mínimo:

- `id`
- `paciente` ou `prontuarioMedico`
- `medicamento`
- `dosagem`
- `frequencia`
- `dataInicio`
- `dataFim`, quando aplicável
- `ativa`

O módulo deve permitir:

- listar prescrições de uma paciente;
- listar somente prescrições ativas;
- identificar medicamentos em uso;
- acessar dosagem e frequência de cada medicamento.

### Métodos esperados no repositório ou service

Caso a prescrição esteja associada diretamente à paciente:

```java
List<Prescricao> findByPacienteAndAtivaTrue(
        Paciente paciente
);
```

Caso a prescrição esteja associada ao prontuário médico:

```java
List<Prescricao> findByProntuarioMedicoAndAtivaTrue(
        ProntuarioMedico prontuarioMedico
);
```

Também pode ser disponibilizado um método no service:

```java
List<Prescricao> buscarPrescricoesAtivasPorPaciente(
        Paciente paciente
);
```

---

## Integração Futura no Relatório Individual

Após a entrega dos módulos dos integrantes responsáveis, o relatório individual exibirá:

- dados pessoais da paciente;
- consultas realizadas;
- eventos sentinelas registrados;
- medicamentos em uso;
- vacinas já aplicadas.

---

## Integração Futura no Relatório Institucional

Após a entrega do módulo de vacinas, o relatório institucional exibirá:

- indicadores gerais de eventos sentinelas;
- indicadores filtrados por tipo de evento;
- consolidado mensal;
- consolidado anual;
- percentual de vacinação para uma vacina específica.