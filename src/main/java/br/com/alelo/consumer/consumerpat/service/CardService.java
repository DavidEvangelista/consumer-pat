package br.com.alelo.consumer.consumerpat.service;

import br.com.alelo.consumer.consumerpat.dto.BalanceDTO;
import br.com.alelo.consumer.consumerpat.entity.Card;
import br.com.alelo.consumer.consumerpat.respository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;

    public void setBalance(BalanceDTO dto) {
        var card = cardRepository.findCardByCardNumber(dto.getCardNumber()).orElseThrow();
        card.setCardBalance(card.getCardBalance() + dto.getValue());
        cardRepository.save(card);
    }

    public Card save(Card card) {
        Optional<Card> cardOp = cardRepository.findCardByCardNumber(card.getCardNumber());
        if(cardOp.isEmpty()) {
            return cardRepository.save(card);
        }
        return null;
    }
}