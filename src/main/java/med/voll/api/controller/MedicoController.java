package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired //Spring  vai estanciar e passar o atributo dentro da classe controller
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){ //valid  - informar para o Spring se integrar com o Bean validation
        //RequestBody - puxa os dados do corpo da requisição.
        repository.save(new Medico(dados));
    }

    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size =10) Pageable paginacao){
        //page - devolverá informações sobre a paginação
        //PedeableDefault, customiza a paginação
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        /*
        Seguindo o padrão de nomeclarura, o Spring consegue montar a consulta,
        gerar a query, o comando sql. findAllBy(nome do atributo).
         */
    }
    /* para paginação, inserir na url
    ordenação: inserir na url "?sort=nomeAtributo
     */

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedicos dados){
        var medico = repository.getReferenceById(dados.id()); //para carregar os dados do BD.
        medico.atualizarInformacoes(dados);
    }

    /*Exclusão lógica: não apaga o registro de fato do BD.
        Apenas sinaliza que não está ativo, para de exibir no sistema.*/
    @DeleteMapping ("/{id}") //complento url dinâmico
    @Transactional
    public void excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id); //recuperar os dados do bd
        medico.excluir(); //modifircará o atributo para inativo
    }
}
