# Text Alarm

![alarm](My-alarm-clock.jpg)

## Description
We are going to use Stripe and Twilio to accomplish something simple.  You are going to write a system that will charge a user money to text them in the morning.  This will help you understand Stripe, Twilio and scheduling with spring.

## Requirements
* Create a gradle app using mustache, jpa, postgres, h2, devtools, web
* The user will need to be able to sign up for the service.  Collect the following information on a registration page:
	* Name
	* Email
	* Phone number
	* Username
	* Password
* Make sure they can log in to the site after signing up
* Once they have logged in offer them to pay $20 to sign up for the service (simple stripe form)
	* Make sure to get the token if they submit the form
	* Use the token to charge them $20
	* Save the charge id returned from stripe into the user's object
* If they have paid then take them to the scheduling page
	* Display the current time they have scheduled
	* Offer a form that will take an hour (24 hour time to be simple) to send them a text
	* Make sure to update the User object with their time
* Schedule a task with the spring schedule that runs every hour
	* Find all users that asked to be notified for that hour
	* Send a twilio SMS message to each user

## Hard Mode
* Create the ability to shut off the notifications
* Allow a way to refund the money back to the user.
* Figure out how to call the user and play a nice message of greeting
* Give the user the option to get the phone call instead of the text

## Nightmare mode
* Integrate with a 3rd party email system like sendgrid or mandril
	* Send an email when the user signs up
	* Send an email to confirm payment
	* Send an email to confirm refund
	* Send an email when the alarm happens

## Resources
* [Github Repo](https://github.com/tiy-lv-java-2016-11/text-alarm)
* [Stripe Documentation](https://stripe.com/docs)
* [Twilio for Java](https://github.com/twilio/twilio-java)
* [Spring Scheduling](https://spring.io/guides/gs/scheduling-tasks/) 
