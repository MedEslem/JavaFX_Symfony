<?php

namespace App\Form;

use App\Entity\User;
use Doctrine\DBAL\Types\TextType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\IsTrue;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\NotBlank;

class RegistrationFormType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('email', EmailType::class)
            ->add('username',\Symfony\Component\Form\Extension\Core\Type\TextType::class,
                ['empty_data' => ''])
            ->add('agreeTerms', CheckboxType::class, [
                'mapped' => false,
                'constraints' => [
                    new IsTrue([
                        'message' => 'You should agree to our terms.',
                    ]),
                ],
            ])
            ->add('Password', PasswordType::class, [
                // instead of being set onto the object directly,
                // this is read and encoded in the controller
                'mapped' => false,
                'attr' => ['autocomplete' => 'off'],
                'constraints' => [
                    new NotBlank([
                        'message' => 'Enter a Password',
                    ])
                ],
            ])
            ->add('confirmPassword', PasswordType::class, [
                // Not mapped directly, used for password confirmation
                'mapped' => false,
                'attr' => ['autocomplete' => 'off'], // Prevent browser autofill
                'constraints' => [
                    new NotBlank([
                        'message' => 'Please confirm your password',
                    ])
                ],
            ])
            ->add('ImagePath', FileType::class, [
                'label' => 'Your Image (png, jpg)',

                'attr' => [
                    'accept' => '.pdf, .png, .jpg', // Types MIME autorisés
                ],
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class,
        ]);
    }
}