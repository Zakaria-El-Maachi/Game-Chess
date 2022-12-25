package com.example.games;

import static java.lang.Math.abs;
import static java.lang.Math.signum;


public class Queen extends Piece{

	public Queen(boolean color) {
		super(color);
		int offset = 10;
		if (color){
			offset = 4;
		}
		setImage(correspondance[offset]);
	}

	@Override
	public boolean validMove(Square start, Square end, Square[][] board) {
		boolean result = true;
		int x1 = start.getCoords()[0];
		int x2 = end.getCoords()[0];
		int y1 = start.getCoords()[1];
		int y2 = end.getCoords()[1];
		
		if (x1 == x2 && y1 == y2) {
			result = false;
		}
		else if (x1 == x2) {
			for(int i = 1 ; i < abs(y2-y1); i++) {
				if (board[x1][(int)(y1 + i * signum(y2-y1))].getContent() != null) {
					result = false;
					break;
				}
			}
			if (end.getContent() != null) {
				if (end.getContent().getColor() == this.getColor()) {
					result = false;
				}
			}
		}
		else if (y1 == y2) {
			for(int i = 1; i < abs(x2-x1); i++) {
				if (board[(int)(x1 + i * signum(x2-x1))][y1] != null) {
					result = false;
					break;
				}
			}
			if (end.getContent() != null) {
				if (end.getContent().getColor() == this.getColor()) {
					result = false;
				}
			}
		}
		else if (abs(x2-x1) == abs(y2-y1)) {
			for(int i = 1; i<abs(x2-x1); i++) {
				if (board[(int)(x1+i*signum(x2-x1))][(int)(y1+i*signum(y2-y1))].getContent() != null) {
					result = false;
				}
			}
			if(end.getContent() != null) {
				if (end.getContent().getColor() == this.getColor()) {
					result = false;
				}
			}
		}
		else {
			result = false;
		}
		return result;
	}

}
