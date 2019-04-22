package org.hemant.thakkar.financialexchange.participants.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hemant.thakkar.financialexchange.participants.domain.Participant;
import org.springframework.stereotype.Service;

@Service("participantMemoryRepositoryImpl")
public class ParticipantMemoryRepositoryImpl implements ParticipantRepository {

	Map<Long, Participant> participants;
	
	public ParticipantMemoryRepositoryImpl() {
		participants = new ConcurrentHashMap<Long, Participant>();
	}
	
	@Override
	public long saveParticipant(Participant participant) {
		participants.put(participant.getId(), participant);
		return participant.getId();
	}

	@Override
	public boolean deleteParticipant(long participantId) {
		Participant participant = participants.get(participantId);
		return participant != null;
	}

	@Override
	public Participant getParticipant(long participantId) {
		return participants.get(participantId);
	}

	@Override
	public int getCount() {
		return participants.size();
	}

	
}
