package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.dto.ExtractDTO;
import br.com.alelo.consumer.consumerpat.entity.Card;
import br.com.alelo.consumer.consumerpat.entity.Extract;
import br.com.alelo.consumer.consumerpat.entity.enums.EstablishmentType;
import br.com.alelo.consumer.consumerpat.exception.BusinessSaldoException;
import br.com.alelo.consumer.consumerpat.respository.CardRepository;
import br.com.alelo.consumer.consumerpat.respository.ExtractRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ExtractDrugstoreService implements ExtractStrategy {

    private final CardRepository cardRepository;
    private final ExtractRepository extractRepository;

    @Override
    public void buy(ExtractDTO dto) throws BusinessSaldoException {
        Optional<Card> cardOp = cardRepository.findCardByCardNumber(dto.getCardNumber());
        if(cardOp.isPresent()) {
            var card = cardOp.get();
            if(dto.getValue() > card.getCardBalance()) {
                throw new BusinessSaldoException("Saldo insuficiente para efetuar a compra!");
            }
            card.setCardBalance(card.getCardBalance() - dto.getValue());
            cardRepository.save(card);
            extractRepository.save(Extract
                    .builder()
                    .value(dto.getValue())
                    .card(card)
                    .dateBuy(new Date())
                    .establishment(EstablishmentType.DRUGSTORE)
                    .productDescription(dto.getProductDescription())
                    .build());
        }
    }

    @Override
    public EstablishmentType getStrategyName() {
        return EstablishmentType.DRUGSTORE;
    }
}
