<?php

namespace App\Controller;

use App\Form\ContactType;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mime\Address;
use Symfony\Component\Mime\Email;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Mailer\MailerInterface;


class ContactController extends AbstractController
{
    #[Route('/contact', name: 'app_contact')]
    public function index(Request $request, MailerInterface $mailer): Response
    {
        $form=$this->createForm(ContactType::class);
        $form->handleRequest($request);
        if($form->isSubmitted()&& $form->isValid()){
            $sujet=$form->get('sujet')->getData();
            $contenue=$form->get('contenue')->getData();
            $email = (new Email())
                ->from(new Address('mohamedeslem.somrani@esprit.tn', 'Al3ab Games'))
                ->to('slouma.somrani@gmail.com')
                ->subject($sujet)
                ->text($contenue);
            $mailer->send($email);
            flash()->addSuccess('Email Has been Sent Successfully ');
            return $this->redirectToRoute('front');
        }
        return $this->renderForm('contact/index.html.twig', [
            'controller_name' => 'ContactController',
            'form'=>$form
        ]);
    }
}