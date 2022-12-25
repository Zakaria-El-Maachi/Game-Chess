package com.example.games;

import static java.lang.Math.abs;

public class King extends Piece{

	public King(boolean color) {
		super(color);
		int offset = 11;
		if (color){
			offset = 5;
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
		else if (abs(x2 - x1)<= 1 && abs(y2 - y1) <= 1){
			if(end.getContent()!= null) {
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
