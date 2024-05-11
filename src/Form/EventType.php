<?php

namespace App\Form;

use App\Entity\Event;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;



class EventType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('Titre_event')
            ->add('Prix_event')
            ->add('Nbr_personnes')
            ->add('Description_event',TextareaType::class)
            ->add('Date_event')
            ->add('Etat_event', ChoiceType::class, [
                'choices' => [
                    'Available' => 'Available',
                    'Not available'=> 'Not available'
                ]
            ])
            ->add('Lieu_event')
            ->add('Categorie_Event')
            ->add('Submit',SubmitType::class)
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Event::class,
        ]);
    }
}