import { Component, OnInit } from "@angular/core";
import { QuizService } from "../quiz.service";
import { Quiz } from "./quiz";
import { ActivatedRoute } from "@angular/router";
import { map } from "rxjs/operators";
import { Observable } from "rxjs";

@Component({
  selector: "app-quiz",
  templateUrl: "./quiz.component.html",
  styleUrls: ["./quiz.component.css"]
})
export class QuizComponent implements OnInit {
  subjectId: any = 20;
  quizQuestions: Quiz;
  questions;
  answers;
  options;
  answer ;
  ans;
  firstLoad = true;
  question: any;
  option1: any;
  option2: any;
  option3: any;
  option4: any;
  index: number = 0;
  totalQuestions = 0;
  sub: any;
  answerSet: any[];
  constructor(
    private quizService: QuizService,
    private route: ActivatedRoute
  ) {
    const id: Observable<string> = this.route.params.pipe(map(p => p.id));
    console.log("id");
    console.log(id);
    const user = this.route.data.pipe(map(d => d.user));
    console.log("user");
    console.log(user);
  }

  ngOnInit() {
    console.log("start on init");
    this.sub = this.route.data.subscribe(v => console.log(v));
    console.log("quiz component on init :" + this.subjectId);

    this.quizService.loadQuizQuestionsBySub(this.subjectId).subscribe(
      (questions1: any[]) => {
        this.questions = questions1;
        console.log("1");
        console.log(questions1);
      },
      error => console.log(error)
    );

    this.quizService.loadAnswersBySub(this.subjectId).subscribe(
      (data1: any[]) => {
        this.answers = data1;
        console.log(data1);
      },
      error => console.log(error)
    );
    this.quizService.loadOptionsBySub(this.subjectId).subscribe(
      (data1: any[]) => {
        this.options = data1;
        console.log(data1);
        this.index = 0;
          this.loadQuestion(this.index);  
      },
      error => console.log(error)
    );
     
    /*
    this.quizService.loadQuizBySub(this.subjectId)
			.subscribe( 
				(questions1: any) => {
					this.quizQuestions = questions1;
          console.log('in component ');  
          console.log(questions1);  
          this.index = 0;
          this.loadQuestion(this.index);   
          this.totalQuestions=  this.quizQuestions.mcqList.length ; 
          this.answerSet =  this.quizQuestions.answers;
				},
				(error) => console.log(error)
		);
*/
  }
  loadQuestion(index) {
    //if(this.firstLoad){
    //  this.firstLoad=false;
    //let optIndex = 0;
    this.question = this.questions[index].desc;
    console.log(this.question);
    let answer1 = this.answers.filter( e => e.id == this.questions[index].id ); 
    this.answer =answer1[this.getRandomInt(answer1.length)].desc;
    // let optionsById = this.options.filter( e => e.id == this.questions[index].id ); 

    let optionsById = this.options.filter( e => {
			
			
			for (let f in e.id   ){
				// console.log(f);				
				if (e.id[f]==this.questions[index].id ){
					return true;
				}
			}
			return false;
		}
	)
    //console.log(answer1[getRandomInt(answer1.length)]);
    this.option1 = optionsById[0].desc;
    this.option2 = optionsById[1].desc;
    this.option3 = optionsById[2].desc;
    this.option4 = optionsById[3].desc;
    this.ans = this.getRandomInt(5);
    switch(this.ans){
       case  0:
       this.ans=1;
      case  1:
        this.option1 =  this.answer ;
      break;

      case 2 :
        this.option2 =  this.answer ;
      break;

      case 3 :
        this.option3 =  this.answer ;
      break;

      case 4 :
        this.option4 =  this.answer ;
      break;

      default :
      console.log('default');
      break;
    }
    //}
  }
  next() {
    console.log("next");
    document.getElementById("opt1").style.backgroundColor = "lightblue";
    document.getElementById("opt2").style.backgroundColor = "lightblue";
    document.getElementById("opt3").style.backgroundColor = "lightblue";
    document.getElementById("opt4").style.backgroundColor = "lightblue";
    this.loadQuestion(++this.index);
  }

  previous() {
    console.log("previous");
    this.loadQuestion(--this.index);
  }

  opt1() {
    //let ans = this.answerSet[this.index].answerId;
    console.log("ans" + this.ans);
    document.getElementById("opt1").style.backgroundColor = "red";
    document.getElementById("opt2").style.backgroundColor = "red";
    document.getElementById("opt3").style.backgroundColor = "red";
    document.getElementById("opt4").style.backgroundColor = "red";
    document.getElementById("opt" + this.ans).style.backgroundColor = "green";
  }
    getRandomInt(max) {
    let randomInt=  Math.floor(Math.random() * Math.floor(max));
    return randomInt;
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
