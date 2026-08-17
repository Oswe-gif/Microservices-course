package com.eazybytes.cards.service.impl;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.entity.Cards;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFoundException;
import com.eazybytes.cards.mapper.CardsMapper;
import com.eazybytes.cards.respository.ICardsRepository;
import com.eazybytes.cards.service.ICardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    ICardsRepository repository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards= repository.findByMobileNumber(mobileNumber);
        if(optionalCards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
        }
        repository.save(createNewCard(mobileNumber));
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = repository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards cards = repository.findByMobileNumber(cardsDto.getMobileNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", cardsDto.getCardNumber())
        );

        CardsMapper.mapToCards(cardsDto, cards);
        repository.save(cards);
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        repository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );

        repository.deleteByMobileNumber(mobileNumber);
        return true;
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }
}
