package com.java.biblioteca.service;

import com.java.biblioteca.domain.Livro;
import com.java.biblioteca.domain.Locar;
import com.java.biblioteca.domain.Movimentos;
import com.java.biblioteca.repository.LivroRepository;
import com.java.biblioteca.repository.LocarRepository;
import com.java.biblioteca.repository.MovimentosRepository;
import com.java.biblioteca.service.dto.LocarDTO;
import com.java.biblioteca.service.dto.LocarDtO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final LivroRepository livroRepository;

    private final LivroService livroService;
    private final LocarRepository locarRepository;
    private final MovimentosRepository movimentosRepository;

    public BookService(MovimentosRepository movimentosRepository, LivroRepository livroRepository, LivroService livroService, LocarRepository locarRepository) {
        this.livroRepository = livroRepository;
        this.livroService = livroService;
        this.locarRepository = locarRepository;
        this.movimentosRepository = movimentosRepository;
    }

    public Locar save(Locar locar) {
        return locarRepository.save(locar);
    }

    public Movimentos save(Movimentos movimentos) {
        return movimentosRepository.save(movimentos);
    }

    public Livro returnLivro(Long id) {
        return livroRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Livro não econtrado"));
    }

    public Movimentos returnMovimentos(Long id) {
        return movimentosRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Not Found"));
    }

    public Locar livroAlugado(String user) {
        return locarRepository.findByProprietario(user).orElse(new Locar());
    }

    public String inativarLivro(Long id, String user) {
        Livro livro = this.returnLivro(id);
        Locar locar = locarRepository.findById(id).orElse(new Locar());
        if  (!livro.getProprietario().equals(user)) {
            return "Você não possui permissão";
        }
        if (locar.getDevolucao() != 1) {
            return "O livro está locado";
        }
        if (livro.getAtivo() != 1) {
            return "O livro já está inativado";
        }
        livro.setAtivo(Long.valueOf(0));
        livroRepository.save(livro);
        return "Livro inativado com sucesso";
    }

    public String ativarLivro(Long id, String user) {
        Livro livro = this.returnLivro(id);
        if (!livro.getProprietario().equals(user)) {
            return ("Você não possui permissão");
        }
        livro.setAtivo(Long.valueOf(1));
        livroRepository.save(livro);
        return ("Livro ativado com sucesso");
    }

    public String locarLivro(Long id, String user, LocarDtO locarDtO) throws ParseException {
        var existsBook = livroRepository.existsById(id);
        if (!existsBook) {
            return "Livro não econtrado";
        }
        Livro livro = this.returnLivro(id);
        var movimentos = new Movimentos();
        var locarLivro = this.livroAlugado(user);
        var livroLocado = locarRepository.findByIdLivro(id.intValue()).orElse(new Locar());

        if (livro.getAtivo() != 1) {
            return ("O livro está inativo");
        }
        if (locarLivro.getDevolucao() != 1) {
            return ("Você já possui um livro alugado");
        }
        if (livroLocado.getDevolucao() != 1) {
            return ("O livro já está alugado");
        }
        var dataAtual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy/MM/dd");
        var previsaDevolucao = sdf.parse(locarDtO.getPrevisao_devolucao());
        if (previsaDevolucao.before(dataAtual)) {
            return ("A previsão de devolução está inválida");
        }
        int m = (int) 0;
        LocalDate date1 = LocalDate.ofInstant(previsaDevolucao.toInstant(), ZoneId.systemDefault());
        locarLivro.setIdLivro(livro.getId().intValue());
        locarLivro.setPrevisaoDevolucao(date1);
        locarLivro.setStatus("ABERTO");
        locarLivro.setDevolucao(m);
        locarLivro.setDataLocacao(LocalDate.now(ZoneId.of("UTC")));
        locarLivro.setLocacao(0);
        locarLivro.setProprietario(user);
        this.save(locarLivro);

        movimentos.setIdLivro(livro.getId().intValue());
        movimentos.setPrevisaoDevolucao(date1);
        movimentos.setStatus("ABERTO");
        movimentos.setDevolucao(m);
        movimentos.setDataLocacao(LocalDate.now(ZoneId.of("UTC")));
        movimentos.setLocacao(0);
        movimentos.setProprietario(user);
        this.save(movimentos);
        return "Livro locado com sucesso";
    }

    public String devolverLivro(Long id, String user) {
        var movimentos = movimentosRepository.findByStatusAndProprietario("ABERTO", user);
        var isAlugadoUser = locarRepository.existsByProprietario(user);
        var isAlugado = locarRepository.existsByIdLivro(id.intValue());
        if (!isAlugado && !isAlugadoUser) {
            return "Você não locou este livro";
        }
        int m = (int) 1;
        locarRepository.delete(locarRepository.findByIdLivro(id.intValue()).get());
        movimentos.setDataDevolucao(LocalDate.now(ZoneId.of("UTC")));
        movimentos.setStatus("CONCLUIDO");
        movimentos.setDevolucao(m);
        movimentos.setLocacao(m);
        this.save(movimentos);
        return "Livro devolvido com sucesso";
    }

    public Object umHistorico(String user) {
        return movimentosRepository.findAllByProprietario(user);
    }

    public List<Movimentos> historico() {
        return movimentosRepository.findAll();
    }

}
