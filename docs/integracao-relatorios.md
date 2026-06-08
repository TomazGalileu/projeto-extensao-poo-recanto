# Contrato de integração do módulo de relatórios

## Objetivo

Definir os dados necessários para integrar os módulos de prontuário e vacinas aos relatórios individuais e institucionais.

## Módulo de vacinas

A entidade Vacina deve possuir, no mínimo:

- id
- paciente
- identificacaoVacina
- dataAplicacao

O módulo deve permitir:

- listar vacinas de uma paciente;
- buscar registros por identificação da vacina;
- calcular quantas pacientes distintas receberam determinada vacina.

Métodos esperados no repositório ou service:

```java
List<Vacina> findByPacienteOrderByDataAplicacaoDesc(Paciente paciente);

List<Vacina> findByIdentificacaoVacinaIgnoreCase(String identificacaoVacina);