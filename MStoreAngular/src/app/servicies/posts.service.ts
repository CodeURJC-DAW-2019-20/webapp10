import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Post } from 'src/app/models/post.model';

const POSTS_URL = "'https://localhost:8443/api/posts/";
@Injectable({ providedIn: 'root' })
export class PostsService {

    constructor(private httpClient: HttpClient) { }

	getPosts(): Observable<Post[]> {
		return this.httpClient.get(POSTS_URL).pipe(			
			catchError(error => this.handleError(error))
		) as Observable<Post[]>;	
	}

	private handleError(error: any) {
		console.error(error);
		return Observable.throw("Server error (" + error.status + "): " + error.text())
	}

}