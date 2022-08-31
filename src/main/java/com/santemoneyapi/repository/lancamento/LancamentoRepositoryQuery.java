package com.santemoneyapi.repository.lancamento;


import com.santemoneyapi.model.Lancamento;
import com.santemoneyapi.repository.LancamentoRepository;
import com.santemoneyapi.repository.filter.LancamentoFilter;

import java.util.List;

public interface LancamentoRepositoryQuery {

    public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);

}
