package com.example.games;

import static java.lang.Math.abs;

public class Pawn extends Piece{
	
	public Pawn(boolean color) {
		super(color);
		int offset = 6;
		if (color){
			offset = 0;
		}
		setImage(correspondance[offset]);
	}

	@Override
	public boolean validMove(Square start, Square end, Square[][] board) {
		boolean result = false;
		int x1 = start.getCoords()[0];
		int x2 = end.getCoords()[0];
		int y1 = start.getCoords()[1];
		int y2 = end.getCoords()[1];
		
		int direction = 1;
		if (!this.getColor()) {
			direction = -1;
		}
		if(end.getContent()==null && x1 == x2){
			if(y2 - y1 == direction){
				result = true;
			}
			else if (y2 - y1 == 2*direction && y2 == (7-direction)/2 && board[x1][y1+direction].getContent() == null){
				result = true;
			}
		}
		else if((end.getContent()!=null && end.getContent().getColor() != this.getColor()) || (end.getGhostContent()!=null && end.getGhostContent().getColor() != this.getColor())) {
			if(abs(end.getCoords()[0]-start.getCoords()[0]) == 1 && end.getCoords()[1] - start.getCoords()[1] == direction){
				result = true;
			}
		}
		return result;
	}
}
