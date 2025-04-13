package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired //Spring  vai estanciar e passar o atributo dentro da classe controller
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBiulder){ //valid  - informar para o Spring se integrar com o Bean validation
        //RequestBody - puxa os dados do corpo da requisição.
        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBiulder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
        //cod 201: requisição processada e novo recurso criado. Devolver os dados e o cabeçalho do prot. HTTP
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size =10) Pageable paginacao){
        //page - devolverá informações sobre a paginação
        //PedeableDefault, customiza a paginação
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
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
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedicos dados){
        var medico = repository.getReferenceById(dados.id()); //para carregar os dados do BD.
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    /*Exclusão lógica: não apaga o registro de fato do BD.
        Apenas sinaliza que não está ativo, para de exibir no sistema.*/
    //Código 204 (mais apropriado para exclusão) - requisição processada e sem conteúdo
    @DeleteMapping ("/{id}") //complento url dinâmico
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id); //recuperar os dados do bd
        medico.excluir(); //modifircará o atributo para inativo

        return ResponseEntity.noContent().build();
    }

    @GetMapping ("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

}
