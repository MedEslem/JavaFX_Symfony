<?php

namespace App\Controller;

use App\Entity\Reservation;
use App\Form\ReservationType;
use App\Repository\ReservationRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Dompdf\Dompdf;
use Dompdf\Options;


#[Route('/reservation')]
class ReservationController extends AbstractController
{
    #[Route('/', name: 'app_reservation_index', methods: ['GET'])]
    public function index(ReservationRepository $reservationRepository): Response
    {
        return $this->render('reservation/index.html.twig', [
            'reservations' => $reservationRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_reservation_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager, MailerInterface $mailer): Response
    {
        $reservation = new Reservation();
        $form = $this->createForm(ReservationType::class, $reservation);
        $form->handleRequest($request);
        $reservation->setUser($this->getUser());
        $user=$this->getUser();

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($reservation);
            $entityManager->flush();
            $email = (new TemplatedEmail())
                ->from(new Address('mohamedeslem.somrani@esprit.tn', 'Al3ab Games'))
                ->to($user->getUserIdentifier())
                ->subject('Your Reservation')
                ->htmlTemplate('reservation/email.html.twig')
            ;
            $mailer->send($email);
            flash()->addSuccess('Thank you for your reservation. You will get your confirmation via E-mail.');

            return $this->redirectToRoute('front', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reservation/new.html.twig', [
            'reservation' => $reservation,
            'form' => $form,
        ]);
    }




    #[Route('/print', name: 'app_reservation_print', methods: ['GET'])]
    public function print( ReservationRepository $reservationRepository)
    {

        $result = $reservationRepository->findAll();
        $pdfOptions = new Options();
        $dompdf = new Dompdf($pdfOptions);
        $html = $this->renderView('reservation/print.html.twig', [
            'reservation' => $result
        ]);
        $dompdf->loadHtml($html);
        $dompdf->setPaper('A4', 'portrait');
        $dompdf->render();
        return new Response($dompdf->output(), Response::HTTP_OK, [
            'Content-Type' => 'application/pdf',
        ]);

    }


    #[Route('/{id_reservation}/edit', name: 'app_reservation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Reservation $reservation, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ReservationType::class, $reservation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            flash()->addSuccess('Reservation Modified Successfully');


            return $this->redirectToRoute('app_reservation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reservation/edit.html.twig', [
            'reservation' => $reservation,
            'form' => $form,
        ]);
    }

    #[Route('/{id_reservation}', name: 'app_reservation_delete')]
    public function delete(ReservationRepository  $repo,$id_reservation, ManagerRegistry $mr): Response
    {

        $reservation=$repo->find($id_reservation);
        $em=$mr->getManager();
        flash()->addWarning('You just deleted a Reservation');
        $em->remove($reservation);
        $em->flush();



        return $this->redirectToRoute('app_reservation_index');
    }
}