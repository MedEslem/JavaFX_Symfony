<?php

namespace App\Controller;

use App\Entity\Event;
use App\Form\EventType;
use App\Repository\EventRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\Persistence\ManagerRegistry;
use Dompdf\Dompdf;
use Dompdf\Options;



#[Route('/event')]
class EventController extends AbstractController
{
    #[Route('/', name: 'app_event_index', methods: ['GET'])]
    public function index(EventRepository $eventRepository): Response
    {
        return $this->render('event/index.html.twig', [
            'events' => $eventRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_event_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $event = new Event();
        $form = $this->createForm(EventType::class, $event);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($event);
            $entityManager->flush();
            flash()->addSuccess('Your Event has been successfully added');

            return $this->redirectToRoute('app_event_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('event/new.html.twig', [
            'event' => $event,
            'form' => $form,
        ]);
    }

    #[Route('/print', name: 'app_event_print', methods: ['GET'])]
    public function print( EventRepository $eventRepository)
    {

        $result = $eventRepository->findAll();
        $pdfOptions = new Options();
        $dompdf = new Dompdf($pdfOptions);
        $html = $this->renderView('event/print.html.twig', [
            'event' => $result
        ]);
        $dompdf->loadHtml($html);
        $dompdf->setPaper('A4', 'portrait');
        $dompdf->render();
        return new Response($dompdf->output(), Response::HTTP_OK, [
            'Content-Type' => 'application/pdf',
        ]);

    }
    #[Route('/triNbr', name: 'triNbr')]
    public function triNbr(Request $request, EventRepository $eventRepository): Response
    {
        $tri = $request->query->get('tri', 'asc'); // Par défaut, tri croissant
        $events = $eventRepository->findBy([], ['Nbr_personnes' => $tri]);

        return $this->render('event/index.html.twig', [
            'events' => $events,
        ]);

    }
    #[Route('/{id_event}/edit', name: 'app_event_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Event $event, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(EventType::class, $event);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            flash()->addSuccess('Your Event has been successfully modified');


            return $this->redirectToRoute('app_event_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('event/edit.html.twig', [
            'event' => $event,
            'form' => $form,
        ]);
    }

    #[Route('/{id_event}/delete', name: 'app_event_delete')]
    public function delete(EventRepository  $repo,$id_event, ManagerRegistry $mr): Response
    {
        $event=$repo->find($id_event);
        $em=$mr->getManager();
        $em->remove($event);
        $em->flush();
        flash()->addWarning('You just deleted an event');
        return $this->redirectToRoute('app_event_index');
    }

    #[Route('/listEvents', name: 'listEF', methods: ['GET'])]
    public function listEvents(EventRepository $eventRepository): Response
    {
        return $this->render('event/list.html.twig', [
            'events' => $eventRepository->findAll(),
        ]);
    }


}




