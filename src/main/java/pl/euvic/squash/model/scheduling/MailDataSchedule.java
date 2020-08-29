package pl.euvic.squash.model.scheduling;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.euvic.squash.model.entity.Reservation;
import pl.euvic.squash.model.repository.ReservationRepository;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MailDataSchedule {

    private final JavaMailSender javaMailSender;
    private final ReservationRepository reservationRepository;
    private final Map<Integer, MailDataScheduleHelper> reservationData = new ConcurrentHashMap<>();

    public MailDataSchedule(JavaMailSender javaMailSender, ReservationRepository reservationRepository) {

        this.javaMailSender = javaMailSender;
        this.reservationRepository = reservationRepository;
    }

    @PostConstruct
    private void populateData() {

        reservationRepository.findAll().forEach(reservation -> {
            Date sendDate = getSendDate(reservation);
            reservationData.put(reservation.getIdReservation(), new MailDataScheduleHelper(sendDate, reservation.getUser().getEmail()));
        });
    }

    public void onReservationDeleting(Reservation reservation) {
        reservationData.remove(reservation.getIdReservation());
    }

    public void onReservationAdded(Reservation reservation) {
        Date sendDate = getSendDate(reservation);
        String userMail = reservation.getUser().getEmail();
        reservationData.put(reservation.getIdReservation(), new MailDataScheduleHelper(sendDate, userMail));
    }

    private Date getSendDate(Reservation reservation) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reservation.getStartTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();

    }

    public void sendNotification(Reservation reservation, List<String> emails) {
        String content = "<h3>Court system notification</h3><br>" +
                "Your reservation in Club " + reservation.getCourt().getClub().getName() + " on " + reservation.getCourt().getClub().getAdress() + ", "
                + reservation.getCourt().getClub().getPostalCode()+ " " + reservation.getCourt().getClub().getCity() + " starts in less than 24h. <br>";

        sendEmail(emails, "Court system notification - booking date alert", content);
    }

    public void sendEmail(List<String> to, String title, String content) {

        to.forEach(it -> {

            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(it);
                helper.setReplyTo("mail.sender@onet.pl");
                helper.setFrom("mail.sender@onet.pl");
                helper.setSubject(title);
                helper.setText(content, true);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            javaMailSender.send(mail);

        });

    }

    @Scheduled(fixedRate = 60000)
    public void checkSchedule() {
        Date currentDate = new Date();

        List<Integer> reservationIds = reservationData.entrySet().stream()
                .filter(it -> it.getValue().getSendDate().before(currentDate))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        if (!reservationIds.isEmpty()) {

            new Thread(() -> {

                List<Reservation> reservationList = reservationRepository.findAllById(reservationIds);
                List<String> emailsList = new ArrayList<>();
                for (Reservation reservation : reservationList) {
                    String email = reservationData.get(reservation.getIdReservation()).getEmail();
                    emailsList.add(email);


                    if (emailsList.size() > 0) {
                        try {
                            sendNotification(reservation, emailsList);
                            reservationData.remove(reservation.getIdReservation());
                        } catch (RuntimeException e) {
                            System.out.println("Send exception" + e.getMessage());
                        }
                    }
                }

            }).start();

        }
    }

}
