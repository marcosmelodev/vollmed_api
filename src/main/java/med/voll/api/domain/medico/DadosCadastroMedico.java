package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroMedico(
        @NotBlank(message = "Nome é obrigatório!") //verifica se não é nulo e nem vazio. So para String
        String nome,
        @NotBlank
        @Email(message = "Formato de e-mail é inválido.")
        String email,

        @NotBlank
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}") //de 4 a 6 dígitos
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull
        @Valid //para validar o outro objeto que está como atributo
        DadosEndereco endereco) {
    //para os dados constantes de especialidade, foi criado um ENUM
    //Bean Validation fará as validações dos itens obrigatórios
    //cada anotação determina o tipo de validação
}
