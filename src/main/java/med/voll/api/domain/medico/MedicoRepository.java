package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);
    /* informar os genêricos
      1 - tipo da entidade que o repository vai trabalhar
      2 - tipo de atributo da chave primária da entidade.*/
}
